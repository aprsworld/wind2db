#!/bin/bash
echo "Generating Wind2.jar"
cp ManifestWind2DB.txt Manifest.txt
jar cfm /home/world/distributionSoftware/wind2db/Wind2.jar Manifest.txt *.class 
cp *.jar *.gif *.png config_wind2db_default.ini /home/world/distributionSoftware/wind2db/
rm Manifest.txt

echo "Generating Wind2DBInstall.jar"
cp ManifestWind2DBInstall.txt Manifest.txt
jar cfm /home/world/distributionSoftware/wind2db/Wind2DBInstall.jar Manifest.txt Wind2DBInstall*.class  Ini*.class Window*.class
cp -r rxtxToInstall /home/world/distributionSoftware/wind2db/
rm Manifest.txt
