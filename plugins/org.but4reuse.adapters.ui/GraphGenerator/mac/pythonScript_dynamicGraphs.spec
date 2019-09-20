# -*- mode: python -*-

block_cipher = None


a = Analysis(['pythonScript_dynamicGraphs.py'],
             pathex=['/Users/anasshatnawi/git/Understanding-Code-Variability/implementation/but4reuse/plugins/org.but4reuse.adapters.ui/GraphGenerator'],
             binaries=[],
             datas=[],
             hiddenimports=['numpy.core._dtype_ctypes','matplotli.pyplot','lxml'],
             hookspath=[],
             runtime_hooks=[],
             excludes=[],
             win_no_prefer_redirects=False,
             win_private_assemblies=False,
             cipher=block_cipher,
             noarchive=False)
pyz = PYZ(a.pure, a.zipped_data,
             cipher=block_cipher)
exe = EXE(pyz,
          a.scripts,
          [],
          exclude_binaries=True,
          name='pythonScript_dynamicGraphs',
          debug=False,
          bootloader_ignore_signals=False,
          strip=False,
          upx=True,
          console=True )
coll = COLLECT(exe,
               a.binaries,
               a.zipfiles,
               a.datas,
               strip=False,
               upx=True,
               name='pythonScript_dynamicGraphs')
