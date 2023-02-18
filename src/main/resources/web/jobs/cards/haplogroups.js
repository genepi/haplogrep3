var Tableau20 = ['#4E79A7', '#A0CBE8', '#F28E2B', '#FFBE7D', '#59A14F', '#8CD17D', '#B6992D', '#F1CE63', '#499894', '#86BCB6', '#E15759', '#FF9D9A', '#79706E', '#BAB0AC', '#D37295', '#FABFD2', '#B07AA1', '#D4A6C8', '#9D7660', '#D7B5A6'];

function initChart(id, dataset, index){

  var data = {
    labels: dataset.groups[index].clades,
    datasets: [{
      label: 'Samples',
      data: dataset.groups[index].values,
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

  return new Chart(document.getElementById(id), config);

}

function destroyChart(chart){
  if (chart != undefined){
    chart.destroy();
    window.chart = undefined;
  }
}

function onChangeClassification(){
  var index = $(this).val();
  destroyChart(window.chart);
  window.chart = initChart('plot-haplogroups', statistics, index);
}

//init chart
destroyChart(window.chart);
window.chart = initChart('plot-haplogroups', statistics, 0);
$('#update-plot-haplogroups').on('change', onChangeClassification);

//register visit handler only once.
if (!window.haplogroupsLoaded){
  document.addEventListener("turbolinks:visit", function() {
    destroyChart(window.chart);
  });
  window.haplogroupsLoaded = true;
}
