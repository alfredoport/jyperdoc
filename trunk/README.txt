* The official web page of the project is hosted at:
  
  http://code.google.com/p/jyperdoc/

You can report bugs at:

  http://code.google.com/p/jyperdoc/issues/list

REQUIREMENTS

* Currently jyperdoc works only if you are using GCL. Other Lisps could
  probably be used in the future if a proper socket framework is developed
  for the different flavors of AXIOM.

USE

* To obtain the source code, you will need to have subversion installed 
  and type the following command:

  svn checkout http://jyperdoc.googlecode.com/svn/trunk jyperdoc

* To use JyperDoc, you need to start axserver first. Start 
  Axiom/Fricas/OpenAxiom inside the axserver directory and type:

  cd axserver
  AXIOMsys
  )read jyperdoc.input

Then start your favorite web browser and point it to the following url:

  http://localhost:8085/location-of-jyperdoc/jyperdoc.xhtml

where "location-of-jyperdoc" is the full path the jyperdoc directory.

