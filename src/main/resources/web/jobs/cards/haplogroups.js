class Haplogroups {

  colors = ['#4E79A7', '#A0CBE8', '#F28E2B', '#FFBE7D', '#59A14F', '#8CD17D',
    '#B6992D', '#F1CE63', '#499894', '#86BCB6', '#E15759', '#FF9D9A', '#79706E',
    '#BAB0AC', '#D37295', '#FABFD2', '#B07AA1', '#D4A6C8', '#9D7660', '#D7B5A6'
  ];

  chart;

  dataset;

  index = 0;

  constructor(id) {
    this.id = id;
  }

  setDataset(dataset) {
    this.dataset = dataset;
  }

  setIndex(index) {
    this.index = index;
  }

  draw() {

    this.destroy();

    var group = this.dataset.groups[this.index];

    var data = {
      labels: group.clades,
      datasets: [{
        label: 'Samples',
        data: group.values,
        hoverOffset: 4,
        backgroundColor: group.colors ? group.colors : this.colors
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

    this.chart = new Chart(document.getElementById(this.id), config);

  }

  destroy() {
    if (this.chart != undefined) {
      this.chart.destroy();
      this.chart = undefined;
    }
  }

}
