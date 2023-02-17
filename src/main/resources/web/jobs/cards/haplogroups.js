var chart = undefined;

var Tableau20 = ['#4E79A7', '#A0CBE8', '#F28E2B', '#FFBE7D', '#59A14F', '#8CD17D', '#B6992D', '#F1CE63', '#499894', '#86BCB6', '#E15759', '#FF9D9A', '#79706E', '#BAB0AC', '#D37295', '#FABFD2', '#B07AA1', '#D4A6C8', '#9D7660', '#D7B5A6'];


function drawChart(index){
  var data = {
    labels: statistics.groups[index].clades,
    datasets: [{
      label: 'Samples',
      data: statistics.groups[index].values,
      hoverOffset: 4,
      backgroundColor: Tableau20
    }]
  };

  var config = {
    type: 'pie',
    data: data,
    options: {
      plugins: {
        legend: {
          position: 'right',
          labels: {
            boxWidth: 20
          }
        }
      },
      animation: {
        animateRotate: false
      }
    },
    responsive: true,
    maintainAspectRatio: false
  };

  if (chart != undefined){
    chart.destroy();
  }

  chart = new Chart(
      document.getElementById('plot-haplogroups'), config);
}

$('#update-plot-haplogroups').on('change', function() {
  var index = $(this).val();
  drawChart(index);
})

drawChart(0);
