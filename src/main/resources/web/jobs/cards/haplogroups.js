var statistics = {{json(job.statistics)}}

var data = [{
  values: statistics.groups[0].values,
  labels: statistics.groups[0].clades,
  type: 'pie'
}];

var layout = {
  //height: 600
  //width: 700
  margin: {
    l: 0,
    b: 0,
    t: 0,
    r: 0
  }
};

Plotly.newPlot('plot-haplogroups', data, layout);

$('#update-plot-haplogroups').on('change', function() {
  var index = $(this).val();
  console.log(index);
  var data = [{
    values: statistics.groups[index].values,
    labels: statistics.groups[index].clades,
    type: 'pie'
  }];

  Plotly.newPlot('plot-haplogroups', data, layout);
})
