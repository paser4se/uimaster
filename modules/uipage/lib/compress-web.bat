java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery.js -o web-min/jquery-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-ui.js -o web-min/jquery-ui-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-dataTable.js -o web-min/jquery-dataTable-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-jstree.js -o web-min/jquery-jstree-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/uimaster.js -o web-min/uimaster-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/uimaster-widget.js -o web-min/uimaster-widget-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/uimaster-chart.js -o web-min/uimaster-chart-min.js

cd web-min
del/f/s/q common.js
type jquery-min.js jquery-ui-min.js > temp1
type temp1 jquery-dataTable-min.js > temp2
type temp2 jquery-jstree-min.js > temp3
type temp3 uimaster-min.js > temp4
type temp4 uimaster-widget-min.js > temp5
type temp5 uimaster-chart-min.js > common.js
del/f/s/q temp1
del/f/s/q temp2
del/f/s/q temp3
del/f/s/q temp4
del/f/s/q temp5
cd ..
cp web-min/common.js .