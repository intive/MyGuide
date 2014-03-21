How to test GPS in iOS Simulator
================================

Summary
-------
There are two ways to fake location in iOS apps - **setting it manually** or 
**defining a path** using 
[GPS eXchange files](http://www.topografix.com/gpx.asp). 

Manual method
-------------
First step is to run MyGuide app in iOS Simulator directly from Xcode. To do it 
select *Product->Run* from menu bar. When app loads we get access to location 
settings in both, Xcode and Simulator. The latter lets us simulate any location 
by defining latitude and longitude explicitly *Debug->Location->Custom 
Location*. Simulator location settings are quite restricted as we can use only 
pre-defined routes or define single custom position.

Using GPX path files
--------------------
We can use Xcode to simulate entire path. The simplest method is to create GPX 
file using [path generator](http://www.bikehike.co.uk/mapview.php) and import 
resulting file to Xcode workspace from *Debug->Simulate Location->Add GPX File 
to Workspace*. Next you have to confirm adding file to workspace, click 
"Finish" and finally, select new path from Debug->Simulate Location->*Our path 
name*.

Last step is to remove file using *Source Control->Discard All Changes* This is 
necessary if you want to pull new version of MyGuide since Xcode won't let you 
checkout new version until changes to project are discarded (or commited).

