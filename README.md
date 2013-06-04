###Ammo

This is plugin for fast reading blobs from file. This is similar to jp@gc Raw Data Source, but without slow java.nio, with supported gzipped ammo and store ammo into heap memory.

Performance?

CPU: Intel(R) Core(TM) i7 CPU         930  @ 2.80GHz
JVM: Oracle Hotspot 1.7u21
Apache Jmeter: 2.10

Ammo: 1.8Gb, 5000000 requests, ~350 bytes per request
Gzip Ammo: Previous ammo, but gzipped. 700Mb

![performance](https://docs.google.com/spreadsheet/oimg?key=0Au50JydZm7UjdFdjZmRMblo2TVBwNVRwZ2Mza0lva0E&oid=1&zx=o6heuxt2z4bw)

[google doc table](https://docs.google.com/spreadsheet/ccc?key=0Au50JydZm7UjdFdjZmRMblo2TVBwNVRwZ2Mza0lva0E&usp=sharing)

JMeter functions:

`${__takeAmmo(cartridgeName)}` take ammo from cartridge
`${__createCartridge(cartridgeName, ammofile, chuckSize, storeInHeap, readAsByte)` create new cartridge with cartridgeName from ammofile. Reading with current chuckSize. If you use binary ammo, set readAsByte to true.

Interface for java/beanshell/scala/jython:

`me.schiz.jmeter.ammo.Cartridge.take(cartridgeName)` take ammo from cartridge
`me.schiz.jmeter.ammo.Cartridge.createCartridge(String name, String ammofile, int chuckSize, boolean storeInHeap, boolean readAsByte)` create new cartridge


[example jmx](https://gist.github.com/sch1z0phren1a/5703881)
