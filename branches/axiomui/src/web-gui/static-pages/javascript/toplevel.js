function makeCLString(string) {
  newString = "\""
  for (var i =0; i < string.length; i++) {
    if (string[i] == "\"") {
      newString = newString + "\\\""
    } else {
      newString = newString + string[i];
    }
  }
  return newString + '"';
}
function axiom_eval() {
  var input = document.getElementById('input');
  var command = input.value;
  input.value = "";

  var output_div = document.getElementById('output');

  var input_div = document.createElement('tt');
  input_div.setAttribute('class','axiomInput');
  input_div.appendChild(document.createTextNode(command));
  output_div.appendChild(input_div);

  ajax_toplevel_eval(display_result, true, null, null, makeCLString(command), session_id);
}
function display_result(response) {
  var doc = response.documentElement;
  var texEls = doc.getElementsByTagName('tex');
  var typeEls = doc.getElementsByTagName('type');
  var plotEls = doc.getElementsByTagName('plot');

  var output_div = document.getElementById('output');

  var container = document.createElement('div');
  container.setAttribute('class','container');

  output_div.appendChild(container);

    if (plotEls.length) {
      var plot_el = plotEls.item(0);

      var url = plot_el.getAttribute('src');
      var width = plot_el.getAttribute('width');
      var height = plot_el.getAttribute('height');

      var plot_div = document.createElement('object');
      plot_div.setAttribute('data', url);
      plot_div.setAttribute('type', 'image/svg+xml');
      plot_div.setAttribute('style', 'width:' + (width + 1) + 'cm;height:' + (height + 1) +'cm;');
      container.appendChild(plot_div);
    }

    if (texEls.length != 0) {
      var tex_el = texEls.item(0);
      var tex = tex_el.firstChild.data;

      var tex_div = document.createElement('div');
      tex_div.setAttribute('class','axiomTex');
      tex_div.appendChild(document.createTextNode(tex));
      container.appendChild(tex_div);
      jsMath.ProcessElement(tex_div);
    }

    if (typeEls.length) {
      var type_el = typeEls.item(0);
      var type = type_el.firstChild.data;
      var type_div = document.createElement('tt');
      type_div.setAttribute('class','axiomType');
      type_div.appendChild(document.createTextNode("Type: " + type));
      container.appendChild(type_div);
    }

  window.scrollTo(0, document.height);
  return false;
}
/*
Copyright (c) 2005, Kai Kaminski
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
