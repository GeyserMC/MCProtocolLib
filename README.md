<b><center><h1>mc-protocol-lib</h></center></b>
==========



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
