d3.select("#graph")
.graphviz().width('100%').height('250px').fit(true)
.renderDot('{{graph.toDot("layout=dot, rankdir=LR")}}')
.on("end", interactive);


function interactive() {
  nodes = d3.selectAll('.node');
  nodes.on("click", function () {
    var text = d3.select(this).selectAll('text').text();
    window.location = '{{routeUrl("clades_show", {phylotree:tree.getIdWithVersion(), clade:"'+text+'"})}}'
  });
  nodes.style("cursor", "pointer");
}
