java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery.js -o mob-min/jquery-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-ui.js -o mob-min/jquery-ui-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-mobile.js -o mob-min/jquery-mobile-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-dataTable.js -o mob-min/jquery-dataTable-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/jquery-jstree.js -o mob-min/jquery-jstree-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/uimaster.js -o mob-min/uimaster-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/uimaster-widget.js -o mob-min/uimaster-widget-min.js
java -jar yuicompressor-2.4.8.jar ../src/other/web/js/uimaster-chart.js -o mob-min/uimaster-chart-min.js

cd mob-min
del/f/s/q common-mob.js
type jquery-min.js jquery-ui-min.js > temp1
type temp1 jquery-mobile-min.js > temp2
type temp2 jquery-dataTable-min.js > temp3
type temp3 jquery-jstree-min.js > temp4
type temp4 uimaster-min.js > temp5
type temp5 uimaster-widget-min.js > temp6
type temp6 uimaster-chart-min.js > common-mob.js
del/f/s/q temp1
del/f/s/q temp2
del/f/s/q temp3
del/f/s/q temp4
del/f/s/q temp5
del/f/s/q temp6
cd ..
cp mob-min/common-mob.js .
