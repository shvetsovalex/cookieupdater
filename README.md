# cookieupdater

# Description
Burp Suite Plugin for useful cookie updating. You can set an actual cookie value in, for example, Repeater tab in two clicks.
 
 
 # How to use
After plugin install you just need to start work with it in Editable Burp tabs. For example, if you have the important query in Repeater tab with old invalid cookies, you just need to click on "Update Cookie" menu item and press "Go" button. This is possible if you have query with actual cookies in Proxy History.

![cookieupdater usage](https://github.com/shvetsovalex/cookieupdater/blob/master/images/cookieupdater.gif)

# How to compile
Compiled by jdk 1.7

Example:

* javac -d build src/burp/*.java

* jar cf plugin.jar -C build burp

# Author
* Shvetsov Alexandr (GitHub: ![shvetsovalex](https://github.com/shvetsovalex))
