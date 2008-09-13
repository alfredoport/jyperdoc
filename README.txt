The official web page of the project is hosted at:
  
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

* To use JyperDoc, you need to start Axiom/Fricas/OpenAxiom first. 
  The following example uses OpenAxiom to show how to start JyperDoc.

    For Windows, you can download a version of OpenAxiom for this platform at:

    http://open-axiom.org/download.html

  cd axserver
  open-axiom -nox
  )read jyperdoc.input

Then start your favorite web browser and point it to the following url:

  http://localhost:8085/location-of-jyperdoc/jyperdoc.xhtml

where "location-of-jyperdoc" is the full path to where jyperdoc was placed.

