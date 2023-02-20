d3.select("#graph")
.graphviz().width('100%').height('550px').fit(true)
.renderDot('{{graph.toDot("layout=dot")}}')
.on("end", interactive);


function interactive() {
  nodes = d3.selectAll('.node');
  nodes.on("click", function () {
    var text = d3.select(this).selectAll('text').text();
    window.location = '{{routeUrl("clades_show", {phylotree:tree.getIdWithVersion(), clade:"'+text+'"})}}'
  });
  nodes.style("cursor", "pointer");
}


$(".clickable-tr").on('click', function () {
    var row = $(this).closest("tr")
    window.location = row.data('href');
});
