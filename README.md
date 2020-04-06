turtlefont

# grammar
(point x y)
(polyline point point ... )
(list polyline polyline ...) (list anything anyting...)
(var name definition)
(position vaiable-name x y width height)



# code 

```
byte[] bytes = new byte[10];

String str = new String(bytes, Charset.forName("UTF-8"));

System.out.println(str);

bytes[] bytes = str.getBytes(Charset.forName("UTF-8"))

final int length = sentence.length();
for (int offset = 0; offset < length; ) {
   final int codepoint = sentence.codePointAt(offset);

   // do something with the codepoint

   offset += Character.charCount(codepoint);
   
}

SwingUtilities.invokeLater 

Swing timer 
```

# TODO
support position while designing font/chart 

(done) before play the font/chart, don't need to input an argument 

