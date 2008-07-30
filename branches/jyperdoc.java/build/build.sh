cd ..
cd src
rm -Rf jyperdoc/*.class
javac jyperdoc/MainWindow.java
mkdir ../build/jyperdoc
cp -Rf jyperdoc/*.class ../build/jyperdoc
cp -Rf jyperdoc/pages ../build/jyperdoc
cp -Rf jyperdoc/bitmaps ../build/jyperdoc
cd ..
cd build
jar -cmf MANIFEST.MF jyperdoc-0.1.jar jyperdoc
rm -Rf jyperdoc
