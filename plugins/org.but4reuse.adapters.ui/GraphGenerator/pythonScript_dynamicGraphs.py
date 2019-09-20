# -*- coding: utf-8 -*-
"""
Created on Wed Nov 28 10:08:22 2018
@author: Thomas, Hadjer, Chao
"""
import numpy as np
import matplotlib.pyplot as plt
from lxml import etree
import sys
from matplotlib.patches import Rectangle
import matplotlib.patches as patches
import tkinter as tk
from tkinter import filedialog
import datetime as dt
from matplotlib import gridspec

#parsing du xml
tree = etree.parse(sys.argv[1])
root = tree.getroot()

#recuperation des differentes nodes principales du xml
adaptersName = root.getchildren()[0]
statReuse = root.getchildren()[1]
blockReuseAnalyse = root.getchildren()[2]
blockUniqueAnalyse = root.getchildren()[3]
figureTitle_elem = ""
configTitle = ""

now = dt.datetime.now()
midnight = dt.datetime.combine(now.date(), dt.time())
seconds = (now - midnight).seconds

nbWinAlv2 = 0
nbWinBlv2 = 0
nbWinlv3 = 0
prefs={}
    
#recuperation des préférences utilisées avec l'adapter s'il y en a
if adaptersName.tag == "AdaptersName":
    
    for node in adaptersName.getchildren():
        figureTitle_elem = figureTitle_elem + node.get("name") + " "
        configTitle+='\n' + node.get("name") + ":\n"
        if len(node.getchildren())!=0:
            for option in node.getchildren():
                configTitle+="     "+option.get("name")+": " + option.get("value")+ ":\n"
                if len(option.getchildren())!=0:
                    for optconfig in option.getchildren():
                        configTitle+="          "+optconfig.get("name") + ": "+optconfig.get("value")+"\n"
                else:
                    configTitle+="          No config for this option\n"
        else:
            configTitle+="     No option for this Adapter\n"

if statReuse.tag == "StatReuse":
    listreuse = []
    listapps = []
    
    
    
    
    for node in statReuse.getchildren():
        listreuse.append(float(node.get("levelofreuse").replace(',','.')))
        listapps.append(node.get("name"))

    
        
        
    
    unique = [a - b for a, b in zip([100] * len(listreuse), listreuse)]
    ind0 = np.arange(len(listapps))
    fig0 = plt.figure(num = str(seconds) + " - Percentage reused in artefacts, adapter(s): " + figureTitle_elem, figsize=(12,6))
    gs = gridspec.GridSpec(1, 2, width_ratios=[3, 1]) 
    ax0 = plt.subplot(gs[0])
    
    #creation du graphe avec les pourcentages d'elements reutilisés par artefact avec matplotlib
    reuse = ax0.barh(ind0, listreuse, align='center', picker = True)
    notreuse = ax0.barh(ind0, unique, align = 'center' , picker = True, left = listreuse )
    ax0.set_xlabel('Reuse Percentage')
    ax0.set_ylabel('Artefacts')
    ax0.set_title('Percentage of elements reused in other artefacts, with adapter(s):\n'+figureTitle_elem)
    ax0.set_yticks(ind0)
    ax0.set_yticklabels(listapps)
    ax0.text(1.05,0.2,"Configuration used for adapter(s): "+figureTitle_elem+"\n"+configTitle, verticalalignment='bottom',
                     horizontalalignment='left',
                     fontsize=10,
                     bbox={'facecolor':'white', 'alpha':0.6, 'pad':10},
                     transform=ax0.transAxes)
    Reused = patches.Patch(color = 'blue', label = 'Reused')
    Unique = patches.Patch(color = 'orange', label = 'unique')
    plt.legend(handles = [Reused,Unique], loc='upper right')
    plt.xticks(np.arange(0, 101, 10))
    
    
    #fonction d'évènement du graphe avec les artefacts reutilisant des elements de l'artefact utilisé
    #quand on clique sur une des barres du rgaphe, affiche une fenêtre tkinter contenant les noms des éléments
    def onpick3(event):
        if event.mouseevent.button == 1 and isinstance(event.artist, Rectangle):
            global nbWinlv3
            patch = event.artist
            title = event.canvas.figure.get_axes()[0].get_title()
            project = title[title.find('\n')+1:]
            
            listelems = ElemsReuse[project][event.canvas.figure.get_axes()[0].get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()]
            def save():
                file = filedialog.asksaveasfile(mode='w', defaultextension = ".txt")
                if file != None:

                    data = t.get(1.0, tk.END)
                    file.write(data)
                file.close()
            elems = tk.Tk()
            menu = tk.Menu(elems)
            elems.config(menu=menu)
            filemenu=tk.Menu(menu)
            menu.add_cascade(label="File", menu=filemenu)
            filemenu.add_command(label="Save", command=save)
            elems.title(str(nbWinlv3) + " - Elements from project " +project+ " reused in project: " + event.canvas.figure.get_axes()[0].get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text())
            nbWinlv3+=1
            t = tk.Text(elems)
            s = tk.Scrollbar(elems)
            s.pack(side=tk.RIGHT, fill = tk.Y)
            t.pack()
            s.config(command=t.yview)
            t.config(yscrollcommand=s.set)
            
            for elem in listelems:
                t.insert(tk.END, elem+"\n")
            
            elems.mainloop()
            
            
            
    #fonction d'évènement quand on clique sur une barre du premier graphe: 
    #crée un nouveau graphe avec les artefacts reutilisant des elements de l'artefact en cours d'analyse et le pourcentage de code réutilisé
    #affiche également dans une fenêtre tkinter les éléments uniques de l'artefact analysé
    def onpick1(event):
        
        if event.mouseevent.button == 1 and isinstance(event.artist, Rectangle): 
            global nbWinAlv2 
            patch = event.artist
            fig, ax = plt.subplots(num =str(seconds) + " - " + str(nbWinAlv2) +  " - Percentage shared elements, adapter(s): "+ figureTitle_elem)
            
            ind = np.arange(len(ReuseGraphs[ax0.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()][1]))
            ax.barh(ind, ReuseGraphs[ax0.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()][0], align='center', picker = True)
            ax.set_xlabel('Percentage')
            ax.set_title("Percentage of shared elements reused in each artefact sharing them, for artefact: \n" + ax0.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text())
            ax.set_yticks(ind)
            ax.set_yticklabels(ReuseGraphs[ax0.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()][1])
            ax.set_xticks(np.arange(0, 101, 10))
            #pour chaque graphe créé en cliquant sur le premier graphe, relie ce graphe avec la fonction d'évènement onpick3
            fig.canvas.mpl_connect('pick_event', onpick3)
            
            def save():
                file = filedialog.asksaveasfile(mode='w', defaultextension = ".txt")
                if file != None:

                    data = t.get(1.0, tk.END)
                    file.write(data)
                file.close()
            unique = tk.Tk()
            menu = tk.Menu(unique)
            unique.config(menu=menu)
            filemenu=tk.Menu(menu)
            menu.add_cascade(label="File", menu=filemenu)
            filemenu.add_command(label="Save", command=save)
            unique.title(str(seconds) +" - " + str(nbWinAlv2) + " - Unique elements from project "+ ax0.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text())
            nbWinAlv2+=1
            t = tk.Text(unique)
            s = tk.Scrollbar(unique)
            s.pack(side=tk.RIGHT, fill = tk.Y)
            t.pack()
            s.config(command=t.yview)
            t.config(yscrollcommand=s.set)
            for elem in ElemsUnique[ax0.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()]:
                t.insert(tk.END, elem+"\n")
            plt.show()
            unique.mainloop()
            
    #connection de la première fonction d'évènement avec le rpemier graphe
    fig0.canvas.mpl_connect('pick_event', onpick1)

    ReuseGraphs = {}
    ElemsReuse = {}
    ElemsUnique = {}
    
    #remplissage des différents dictionnaires pour reclasser les infos du xml et faire les graphes interactifs
    for node in statReuse.getchildren():
        if len(node.getchildren())!=0:
            InsideReuse = []
            InsideNames = []
            name = node.get("name")
            ReuseElems = {}
            UniqueElems = []
            for elem1 in node.getchildren()[0].getchildren():
                
                InsideReuse.append(float(elem1.get("levelofreuse").replace(',','.')))
                InsideNames.append(elem1.get("name"))
                Elems = []
                if len(elem1.getchildren())!=0:
                    for elem2 in elem1.getchildren():
                        Elems.append((elem2.get("element")))
                        
                    ReuseElems[elem1.get("name")] = Elems
            for unique in node.getchildren()[1].getchildren():
                UniqueElems.append(unique.get("element"))
            ElemsReuse [name] = ReuseElems
            ElemsUnique [name] = UniqueElems
            ReuseGraphs[name] = (InsideReuse, InsideNames)
      
#la section analyse des blocks est presque la même que celle des artefacts, à quelques détails près
if blockReuseAnalyse.tag == "BlockReuseAnalyse":
    
    

    nb_artefacts = []
    blocks_name = []
    max_nbArtefacts = 0
    for node in blockReuseAnalyse.getchildren():
        n = int(node.get("nbartefacts"))
        nb_artefacts.append(n)
        if max_nbArtefacts<n:
            max_nbArtefacts = n
        blocks_name.append(node.get("name"))
    ind2 = np.arange(len(blocks_name))
    fig2, ax2 = plt.subplots(num = str(seconds) + "Blocks reused, adapter(s): " + figureTitle_elem)
    fig2.suptitle("Blocks reused \n[adapter(s):"+figureTitle_elem+"]")
    reuse = ax2.barh(ind2, nb_artefacts, align='center', picker = True)
    ax2.set_xlabel('Number of artefacts that contains block')
    ax2.set_ylabel('Blocks')
    ax2.set_yticks(ind2)
    ax2.set_yticklabels(blocks_name)
    plt.xticks(np.arange(0, max_nbArtefacts+1, 1))
    
  
        
    
    def onpick2(event):
        
        if event.mouseevent.button == 1  and isinstance(event.artist, Rectangle):
            global nbWinBlv2
            patch = event.artist
            fig3, ax3 = plt.subplots(num=str(seconds) + " - " + str(nbWinBlv2) + " - Percentage of the block in artefacts, adapter(s): "+figureTitle_elem)
            ind = np.arange(len(BlocksReuse[ax2.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()][0]))
            ax3.barh(ind, BlocksReuse[ax2.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()][1], align='center')
            ax3.set_yticks(ind)
            ax3.set_yticklabels(BlocksReuse[ax2.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()][0])
            ax3.set_xlabel('Percentage')
            ax3.set_title("Percentage representation of the block in artefacts \n[adapter(s):"+figureTitle_elem+"]" + ", in block " + ax2.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text())
            ax3.set_xticks(np.arange(0, 101, 10))
            
            elemsb = tk.Tk()
            def save():
                file = filedialog.asksaveasfile(mode='w', defaultextension = ".txt")
                if file != None:

                    data = t.get(1.0, tk.END)
                    file.write(data)
                file.close()
            
            menu = tk.Menu(elemsb)
            elemsb.config(menu=menu)
            filemenu=tk.Menu(menu)
            menu.add_cascade(label="File", menu=filemenu)
            filemenu.add_command(label="Save", command=save)
            elemsb.title(str(seconds) + " - " + str(nbWinBlv2) + " - Elements in block: " + ax2.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text())
            nbWinBlv2 += 1
            t = tk.Text(elemsb)
            s = tk.Scrollbar(elemsb)
            s.pack(side = tk.RIGHT, fill = tk.Y)
            t.pack()
            s.config(command=t.yview)
            t.config(yscrollcommand=s.set)
            
            
            for elem in ElemsBlock[ax2.get_yticklabels()[int(patch.xy[1] + patch.get_height()/2)].get_text()]:
                t.insert(tk.END, elem + "\n")
            plt.show()
            elemsb.mainloop()
            

            
    fig2.canvas.mpl_connect('pick_event', onpick2)

    BlocksReuse = {}
    ElemsBlock = {}
    for node in blockReuseAnalyse.getchildren():
        if len(node.getchildren()[0])!=0:
            artefacts_name3 = []
            percentages3 = []
            elements = []

            for elem in node.getchildren()[0]:
                percentages3.append(float(elem.get("percentage").replace(',','.')))
                artefacts_name3.append(elem.get("name"))
            
            for elem2 in node.getchildren()[1]:
                elements.append(elem2.get("name"))
                
            BlocksReuse[node.get("name")] = (artefacts_name3, percentages3)
            ElemsBlock[node.get("name")] = elements
          
    plt.show()
