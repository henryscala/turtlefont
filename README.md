turtlefont

# grammar
(point x y)
(polyline point point ... )
(list polyline polyline ...) (list anything anyting...)
(var name definition)
(position vaiable-name x y width height)

alternative grammar in java properties file 
```
char=人
numpolyline=2
polyline1=(0.5,0)~(0,1) 
polyline2=(0.333333,0.333333)~(1,1)

x=0
y=0
width=1
height=1

numpicture=1
picture1=join polyline1 polyline2
picture2=position subpicture1 {x} {y} {width} {height}

finalpicture=subpicture1~subpicture2
```

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
(done) support position while designing font/chart 

(done) before play the font/chart, don't need to input an argument 

(done) play all the fonts in a folder 

(done) support variable everywhere(variable can stands for a number, an element, or another variable) 

(done) support blank char 

(done) support rotate 

draw English char 

(done) draw numbers, solve bug on number 8 

(done) draw circle/arc(using polygon to simulate circle/arc) 

(done) draw circle/arc in counter clockwise direction(using polygon and the reverse function to achieve this target).

(done) support register char to the system 

support comment in lispy 

永 char

# some other configuration file format 
consider not inventing my own language, but create a DSL, or using an available JVM script language, or open configuration file standard. Candidates may be: 

- groovy: seems too big?

- java beanshell? 

- schemy: it is for .net. https://github.com/Microsoft/schemy

- jsonnet: The data templating language 

- dhall: It does not have java library to parse. 

- cuelang: https://cuelang.org/. CUE is an open-source data validation language and inference engine with its roots in logic programming. Seems too big. 

- HCL: https://github.com/hashicorp/hcl HCL is the HashiCorp configuration language. It is json compatible. Seems only for golang. 

- HOCON: c. Configuration library for JVM languages.

- https://github.com/mixmastamyk/tconf. It is only for python?

- XProperties: Single file solution. https://github.com/MideO/CucumberJVM/blob/master/src/tests/cukes/stepdefinitions/config/XProperties.java 

- http://norvig.com/lispy.html implement a very small lisp article

- the author implemented http://norvig.com/jscheme.html

- http://alumni.media.mit.edu/~mt/skij/skijdoc.html another scheme implementation in java. A small Scheme interpreter, written in and running in Java, with extensions that allow a user to interactively create, inspect, manipulate, and script arbitrary Java objects. 

- kawa https://www.gnu.org/software/kawa/index.html

- http://sisc-scheme.org/  SISC is an extensible Java based interpreter of the algorithmic language Scheme. 