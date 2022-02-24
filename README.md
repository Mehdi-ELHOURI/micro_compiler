# micro_compiler
 Implementation in _Java_ of a micro compiler including: 
  * Constant and variable declaration. 
  * Simple expressions. 
  * Assignment instruction. 
  * If-then-else condition.
  <hr>
  The micro_compiler consists of 2 phases:
  <ol>
   <li>Analysis phase, which includes:
    <ul>
     <li>Lexical analysis, using a pre-defined regular grammar.</li>
     <li>Syntax analysis, using a context-free grammar.</li>
     <li>Semantic analysis, using syntax directed translation.</li>
    </ul>
   </li>
   <li>Code generation phase: which was integrated in the parser and generates a set of assembly code instructions.</li>
  </ol>
    <hr>
<h2>EBNF representation of the syntax: </h2>

 ![EBNF]( https://user-images.githubusercontent.com/82706421/155603200-5aa1baac-9b7d-4dd2-b678-33f9cf0320c2.JPG )

<h2>Input: </h2>

 ![input]( https://user-images.githubusercontent.com/82706421/155602447-ffb2449d-c552-44da-a720-7d0dfefd959e.JPG )
   
<h2>Output: </h2>

 ![output]( https://user-images.githubusercontent.com/82706421/155602469-943560f6-8311-44bf-9724-717c2f5869cb.JPG )
