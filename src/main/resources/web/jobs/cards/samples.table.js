class SamplesTable {

  id;

  maxMutations = 8;

  table;

  samples;

  phylotree;

  filter = 'all';

  server = '';

  constructor(id) {
    this.id = id;
  }

  setSamples(samples) {
    this.samples = samples;
  }

  setPhylotree(phylotree) {
    this.phylotree = phylotree;
  }

  setServer(serverUrl) {
    this.serverUrl = serverUrl;
  }

  render() {

    this.destroy();

    var self = this;

    this.table = $("#" + this.id).DataTable({
      pageLength: 50,
      lengthChange: false,
      fixedHeader: {
        headerOffset: $('#navigation').outerHeight()
      },
      data: samples,
      "columns": [{
        data: "hasError",
        "render": function(data, type, row) {
          if (type == 'display') {
            return self.renderIcon(row);
          } else {
            return self.renderText(row);
          }
        },
        width: "20px"
      }, {
        "data": "sample"
      }, {
        "data": "clade"
      }, {
        "render": function(data, type, row) {
          return self.renderProgressBar(row.quality);
        }
      }, {
        "data": "ns",
        width: "20px"
      }, {
        "data": "coverage"
      }, {
        "data": "ranges",
        "render": function(data) {
          return data.length + ' ranges';
        }
      }, {
        "data": "annotatedPolymorphisms",
        "render": function(data, type) {
          if (type == 'display') {
            return self.formatMutations(data);
          } else {
            return self.renderMutations(data);
          }
        }
      }],
      order: [
        [1, 'asc']
      ],
      "createdRow": function(row, data, dataIndex) {
        if (data.errors.length > 0) {
          $(row).addClass('table-danger');
          return;
        }
        if (data.warnings.length > 0) {
          $(row).addClass('table-warning');
          return;
        }
      }
    });

    var self = this;

    //check to bind it to a correct instance?
    $.fn.dataTable.ext.search.push(
      function(settings, data, dataIndex) {
        if (self.filter === "all" || data[0] == self.filter) {
          return true;
        }
        return false;
      }
    );

    $('#' + this.id + ' tbody').on('click', 'tr', function() {
      var data = self.table.row(this).data();
      new SamplesDetails(self.phylotree, data, self.serverUrl).show();
    });

  }

  setSampleFilter(filter) {
    this.filter = filter;
    if (this.table != undefined) {
      this.table.draw();
    }
  }

  setMaxMutations(maxMutations) {
    this.maxMutations = maxMutations;
    if (this.table != undefined) {
      this.table.rows().invalidate().draw();
    }
  }

  formatMutations(data) {
    var result = '';
    for (var i = 0; i < data.length && i < this.maxMutations; i++) {
      var label = data[i].nuc;
      var found = data[i].found;

      if (result != '') {
        result += ' ';
      }
      result += '<span class="badge ' + (found ? 'badge-success' : 'badge-info') + '">' + label + '</span>';

    }
    if (data.length > this.maxMutations) {
      result += '<small class="text-muted"> and <b>' + (data.length - this.maxMutations) + '</b> more</small>';
    }
    return result;
  }

  renderMutations(data) {
    var result = '';
    for (var i = 0; i < data.length; i++) {
      var label = data[i].nuc;
      if (result != '') {
        result += ' ';
      }
      result += label;
    };
    return result;
  }

  renderProgressBar(value) {
    var percentage = value * 100;
    return '<div class="progress" style="width: 60px;" title="Quality: ' + value.toFixed(2) + '">' +
      '<div class="progress-bar bg-success" role="progressbar" aria-valuenow="' + percentage + '" aria-valuemin="0" aria-valuemax="100" style="width: ' + percentage + '%">' +
      '<span class="sr-only">Quality: ' + value.toFixed(2) + '</span>' +
      '</div>' +
      '</div>';
  }

  renderIcon(data) {
    if (data.errors.length > 0) {
      return '<i class="fas fa-exclamation-circle text-danger"></i>';
    }
    if (data.warnings.length > 0) {
      return '<i class="fas fa-exclamation-triangle text-warning"></i>';
    }
    return '<i class="fas fa-check-circle text-success"></i>';
  }

  renderText(data) {
    if (data.errors.length > 0) {
      return 'error';
    }
    if (data.warnings.length > 0) {
      return 'warning';
    }
    return 'ok';
  }

  //adjust column size on tab switch
  autoSize() {
    this.table.columns.adjust();
  }

  destroy() {
    if (this.table) {
      this.table.destroy();
    }
  }

}
