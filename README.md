# SpotifyPartyPlayer
This app was my exam assignment for Android programming and the first Android app i developed. You can see my exam report in the master branch.

An Android app i started on to make it easier to queue songs on the playing device from your own device. Uses Spotify SDK and NSD 

In the app/src/main/.../spotifypartyplay you can see my java files. 
Sessions, Songs and Users is the class files i've created to store the information needed. 

While ChatConnection is my own tweaked version of the NsdHelper class, this sends the information about the instance of the different 
connected apps over the wifi, so there is no hosting needed for it to work, only the same wifi connection. 

MainActivity, UserSession, availableSessions, sessionActivity and startSessionActivity is the Android Activities.
From MainActivity you can choose to go to startSessionActivity or availableSessions.
On startSession activity you can setup the session, and start broadcasting it on the wifi. 
On availebleSessions you will be able to see all the sessions broadcasted, then connect to the one you click, it may be password protected or not.
The broadcaster will be sent to UserSession, and the others who connect will be sent to sessionActivity. These are pretty much the same
sessions, only UserSession will have a bit more control. In these sessions they can add spotify uri, to add that song to queue on the broadcaster
phone, but it will be displayed on all the connected devices. 
