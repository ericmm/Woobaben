# Woobaben
Some thoughts in my mind

Talk is cheap, show me the code.

---------------------
<li>ArrayBackedHashMap - inspired by Mikhail Vorontsov's ObjObjMap, faster and less memory consumption than java.util.HashMap (see ArrayBackedHashMapTest.testPerformance())  --Done 
<li>ArrayBackedHashSet - Array Backed Hash Set  --Done 
<li>Faster java object attribute accessors by utlilising sun.misc.Unsafe  --Done 
<li>Faster java heap object copy (clone) by sun.misc.Unsafe  --Done

<b>Near plan</b>
<li>Heap object serialiser and deserialiser (binary format)
<li>Off-heap object facility
<li>Off-heap object copy/serialise/deserialise

<b>Future plan</b>
<li>Integration of Kilim (Java coroutine - lightweight thread)
<li>JNI - use 'javacritical' optimisation with off-heap memory 
<li>And some other more...




