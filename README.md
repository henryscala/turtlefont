turtlefont

# grammar
(point x y)
(polyline point point ... )
(list polyline polyline ...) (list anything anyting...)
(variable name definition)
(scale variable-name factor) (scale definition factor)
(position vaiable-name factor) (position definition factor)



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
```

