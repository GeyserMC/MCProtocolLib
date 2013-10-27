<b><center><h1>mc-protocol-lib</h></center></b>
==========


<b>Discontinued</b>
--------

This project has been discontinued (for now anyway) due to the massive amount of changes made in 1.7's protocol. A lot of things were rewritten, many packet content and id changes occurred, the packet format changed, and now some (current 1 at the time) packets are server/client specific with the same ids. If anyone wants to submit a pull request to update to 1.7, I would be glad to continue, but at the moment its just too much to do.


<b>About mc-protocol-lib</b>
--------

mc-protocol-lib is a simple library for communicating with a Minecraft client/server. It aims to allow people to make custom bots, clients, or servers for Minecraft easily.


<b>Server List Ping</b>
--------

When you receive a server list ping packet when listening to a server, respond by calling connection.disconnect(Util.formatPingResponse(motd, players, maxplayers));

When you are sending a ping request, do the following:
  connection.send(new PacketServerPing());
  connection.send(Util.prepareClientPingData(connection.getHost(), connection.getPort()));


<b>Chat Bot Example</b>
--------

See ch.spacebase.mcprotocol.example.ChatBot


<b>Building the Source</b>
--------

mc-protocol-lib uses Maven to manage dependencies. Simply run 'mvn clean install' in the source's directory.


<b>License</b>
---------

mc-protocol-lib is licensed under the <b>[MIT license](http://www.opensource.org/licenses/mit-license.html)</b>.
