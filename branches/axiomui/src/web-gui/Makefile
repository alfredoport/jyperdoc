TANGLE=${AXIOM}/bin/lib/notangle -t8
WEAVE=${AXIOM}/bin/lib/noweave -delay
LISP=clisp


DEP =	axiom-com.lisp \
	axiom-hub.asd \
	axiom-hub.lisp \
	axiom-plot.lisp \
	cl-svg.lisp \
	defpackage.lisp \
	editor.lisp \
	Makefile \
	make-image.lisp \
	plots.lisp \
	session.lisp \
	static-pages/javascript/toplevel.js \
	toplevel.lisp \
	uidraw.spad \
	welcome.lisp

DOCDEP =axiom-com.lisp.tex \
	axiom-hub.asd.tex \
	axiom-hub.lisp.tex \
	axiom-plot.lisp.tex \
	cl-svg.lisp.tex \
	defpackage.lisp.tex \
	editor.lisp.tex \
	make-image.lisp.tex \
	plots.lisp.tex \
	session.lisp.tex \
	toplevel.js.tex \
	toplevel.lisp.tex \
	uidraw.spad.tex \
	welcome.lisp.tex

std: code compile

all: code doc compile

code: $(DEP)

doc: $(DOCDEP)

compile:
	@echo ")compile uidraw.spad"|AXIOMsys
	${LISP} -norc -i make-image.lisp
	@echo "#!/bin/sh" > run-axiom-ui.sh
	@echo "clisp -M " `pwd`/axiom-ui.mem >> run-axiom-ui.sh
	@chmod a+x run-axiom-ui.sh

clean:
	@rm -r *lisp *spad *asd *tex *dvi DRAW* \
	       *aux *log *mem *toc *fas *lib \
	       libdb.text run-axiom-ui.sh

axiom-com.lisp.tex: axiom-com.lisp.pamphlet
	@${WEAVE}  axiom-com.lisp.pamphlet > axiom-com.lisp.tex
	latex $@
	latex $@

axiom-hub.asd.tex: axiom-hub.asd.pamphlet
	@${WEAVE}  axiom-hub.asd.pamphlet > axiom-hub.asd.tex
	latex $@
	latex $@

axiom-hub.lisp.tex: axiom-hub.lisp.pamphlet
	@${WEAVE}  axiom-hub.lisp.pamphlet > axiom-hub.lisp.tex
	latex $@
	latex $@

axiom-plot.lisp.tex: axiom-plot.lisp.pamphlet
	@${WEAVE}  axiom-plot.lisp.pamphlet > axiom-plot.lisp.tex
	latex $@
	latex $@

cl-svg.lisp.tex: cl-svg.lisp.pamphlet
	@${WEAVE}  cl-svg.lisp.pamphlet > cl-svg.lisp.tex
	latex $@
	latex $@

defpackage.lisp.tex: defpackage.lisp.pamphlet
	@${WEAVE}  defpackage.lisp.pamphlet > defpackage.lisp.tex
	latex $@
	latex $@

editor.lisp.tex: editor.lisp.pamphlet
	@${WEAVE}  editor.lisp.pamphlet > editor.lisp.tex
	latex $@
	latex $@

make-image.lisp.tex: make-image.lisp.pamphlet
	@${WEAVE}  make-image.lisp.pamphlet > make-image.lisp.tex
	latex $@
	latex $@

plots.lisp.tex: plots.lisp.pamphlet
	@${WEAVE}  plots.lisp.pamphlet > plots.lisp.tex
	latex $@
	latex $@

session.lisp.tex: session.lisp.pamphlet
	@${WEAVE}  session.lisp.pamphlet > session.lisp.tex
	latex $@
	latex $@

toplevel.js.tex: toplevel.js.pamphlet
	@${WEAVE}  toplevel.js.pamphlet > toplevel.js.tex
	latex $@
	latex $@

toplevel.lisp.tex: toplevel.lisp.pamphlet
	@${WEAVE}  toplevel.lisp.pamphlet > toplevel.lisp.tex
	latex $@
	latex $@

uidraw.spad.tex: uidraw.spad.pamphlet
	@${WEAVE}  uidraw.spad.pamphlet > uidraw.spad.tex
	latex $@
	latex $@

welcome.lisp.tex: welcome.lisp.pamphlet
	@${WEAVE}  welcome.lisp.pamphlet > welcome.lisp.tex
	latex $@
	latex $@


axiom-com.lisp: axiom-com.lisp.pamphlet
	@${TANGLE}  axiom-com.lisp.pamphlet > axiom-com.lisp

axiom-hub.asd: axiom-hub.asd.pamphlet
	@${TANGLE}  axiom-hub.asd.pamphlet > axiom-hub.asd

axiom-hub.lisp: axiom-hub.lisp.pamphlet
	@${TANGLE}  axiom-hub.lisp.pamphlet > axiom-hub.lisp

axiom-plot.lisp: axiom-plot.lisp.pamphlet
	@${TANGLE}  axiom-plot.lisp.pamphlet > axiom-plot.lisp

cl-svg.lisp: cl-svg.lisp.pamphlet
	@${TANGLE}  cl-svg.lisp.pamphlet > cl-svg.lisp

defpackage.lisp: defpackage.lisp.pamphlet
	@${TANGLE}  defpackage.lisp.pamphlet > defpackage.lisp

editor.lisp: editor.lisp.pamphlet
	@${TANGLE}  editor.lisp.pamphlet > editor.lisp

make-image.lisp: make-image.lisp.pamphlet
	@${TANGLE}  make-image.lisp.pamphlet > make-image.lisp

plots.lisp: plots.lisp.pamphlet
	@${TANGLE}  plots.lisp.pamphlet > plots.lisp

session.lisp: session.lisp.pamphlet
	@${TANGLE}  session.lisp.pamphlet > session.lisp

static-pages/javascript/toplevel.js: toplevel.js.pamphlet
	@${TANGLE}  toplevel.js.pamphlet > static-pages/javascript/toplevel.js

toplevel.lisp: toplevel.lisp.pamphlet
	@${TANGLE}  toplevel.lisp.pamphlet > toplevel.lisp

uidraw.spad: uidraw.spad.pamphlet
	@${TANGLE}  uidraw.spad.pamphlet > uidraw.spad

welcome.lisp: welcome.lisp.pamphlet
	@${TANGLE}  welcome.lisp.pamphlet > welcome.lisp
