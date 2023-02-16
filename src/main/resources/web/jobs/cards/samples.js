window.view = 'nuc';
window.maxMutations = 8;
window.gene = '';

window.table = $(".data-table").DataTable({
  pageLength: 50,
  lengthChange: false,
  fixedHeader: {
    headerOffset: $('#navigation').outerHeight()
  },
  /*ajax: {
    url: '{{routeUrl("jobs_download", {job: job.id, file: "clades.json"})}}',
    dataSrc: ''
  },*/
  data: samples,
  "columns": [{
    data: "hasError",
    "render": function(data, type, row) {
      if (type == 'display'){
        return renderIcon(row);
      } else {
        return renderText(row);
      }
    },
    width: "20px"
  }, {
    "data": "sample"
  }, {
    "data": "clade"
  }, {
    "render": function(data, type, row) {
      return renderProgressBar(row.quality);
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
        if (type == 'display'){
          return formatMutations(data, window.maxMutations, window.view, window.gene);
        } else {
          return renderMutations(data, window.view, window.gene);
        }
      }
    }
  ],
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

$.fn.dataTable.ext.search.push(
  function(settings, data, dataIndex) {
    var samples = $('#show_samples').val();
    if (samples === "all" || data[0] == samples) {
      return true;
    }
    return false;
  }
);

$("#show_samples").change(function(e) {
  window.table.draw();
});

$('.data-table tbody').on('click', 'tr', function() {

  var data = window.table.row(this).data();

  var dialog = bootbox.dialog({
    title: renderIcon(data) + '&nbsp;' + data.sample,
    onEscape: true,
    buttons: {
      close: {
        label: 'Close',
        className: 'btn-primary',
        callback: function() {

        }
      }
    },
    message: '' +
      '<div class="container-fill">' +
      '<div class="row">' +
      '<div class="col-md-8">' +
      '<div class="card" style="padding-right: 0px;">' +
      '<div style="height: 500px; overflow-y: scroll; padding: 10px;">' +
      renderWarningsAndErrors(data) +
      '<b>Top Hit</b>' +
      '<br>'+
      '<b><a href="/phylogenies/{{job.phylotree}}/haplogroups/' + data.clade + '" target="_blank">' + data.clade + '</a></b>' +
      ' (' + data.quality.toFixed(2) * 100 + '%)<br><br>' +
      '<b>Expected Mutations</b>' +
      '&nbsp; '+
      '<a href="https://haplogrep.readthedocs.io/en/latest/mutations/#expected-mutations" target="_blank" title="Help" class="col-form-label"> '+
      '  <i class="fas fa-question-circle"></i> '+
      '</a><br>'+
      formatMutationsNotFound(data.expectedMutations, 'nuc', data.clade) + '<br><br>' +
      '<b>Remaining Mutations</b>' +
      '&nbsp; '+
      '<a href="https://haplogrep.readthedocs.io/en/latest/mutations/#remaining-mutations" target="_blank" title="Help" class="col-form-label"> '+
      '  <i class="fas fa-question-circle"></i> '+
      '</a><br>'+
      formatRemainingMutations(data.remainingMutations, 'nuc', data.clade) + '<br><br>' +
      '<b><a class="" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">Additional Hits</a></b><br>' + formatHits(data) + ' <br>' +
      '<b>Ranges</b><br>' + formatRange(data.ranges) + '<br><br>' +
      //'<b>Amino Acid Changes</b><br>' + formatMutations(data.annotatedPolymorphisms, 500, 'aac', '') + '<br><br>' +
      //'<b>Nucleotide Changes</b><br>' + formatMutations(data.annotatedPolymorphisms, 500, 'nuc', '') + '<br><br>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '<div class="col-md-4">' +
      '<div class="card" style="padding-left:0px;padding-right: 0px;">' +
      '<iframe id="details" style="" width="100%" height="500px" src="" frameborder="0"></iframe>' +
      '</div>' +
      '</div>' +
      '</div>' +
      '</div>'
  });

  dialog.find("div.modal-dialog").addClass("scroll-modal-body-horizontal");

});


$('#amino_acid_changes').on('change', function() {
  if ($(this).is(":checked")) {
    window.view = 'aac';
  } else {
    window.view = 'nuc';
  }
  window.table.rows().invalidate().draw();
});


$('#show_all_mutations').on('change', function() {
  var value = $(this).val();
  if (value == '') {
    window.gene = '';
    window.maxMutations = 8;
  } else if (value == 'all') {
    window.gene = '';
    window.maxMutations = 500;
  } else {
    window.maxMutations = 500;
    window.gene = value;
  }
  window.table.rows().invalidate().draw();
})


function formatMutations(data, maxMutations, view, gene) {

  var result = '';
  for (var i = 0; i < data.length && i < maxMutations; i++) {
    var filtered = false;
    var label = view == 'aac' ? data[i].aac : data[i].nuc;
    if (gene != '') {
      if (data[i].aac == undefined || !data[i].aac.startsWith(gene)) {
        filtered = true;
      }
    }
    if (!filtered && label != undefined && label != '') {
      if (result != '') {
        result += ' ';
      }
      result += '<span class="badge ' + (data[i].found ? 'badge-success' : 'badge-info') + '">' + label + '</span>';
    }
  };
  if (data.length > maxMutations) {
    result += '<small class="text-muted"> and <b>' + (data.length - maxMutations) + '</b> more</small>';
  }
  return result;
}

function renderMutations(data, view) {

  var result = '';
  for (var i = 0; i < data.length; i++) {
    var filtered = false;
    var label = view == 'aac' ? data[i].aac : data[i].nuc;
    if (label != undefined && label != '') {
      if (result != '') {
        result += ' ';
      }
      result += label;
    }
  };
  return result;
}

var selectedMutation = undefined;
function openMutation(source){
  var mutation = $(source).data("mutation");
  var clade = $(source).data("clade");
  if (selectedMutation){
    $(selectedMutation).removeClass("selected-mutation");
  }
  $(source).addClass("selected-mutation");
  var url = '/phylogenies/{{job.phylotree}}/haplogroups/' + clade + '/mutations/' + mutation + '?minimal=true';
  console.log($("#details"));
  $("#details").attr('src', url);
  selectedMutation = source;
}

function formatMutationsNotFound(data, view, clade) {

  var result = '';
  for (var i = 0; i < data.length; i++) {
    var filtered = false;
    var label = view == 'aac' ? data[i].aac : data[i].nuc;
    var id = data[i].position + '_' + data[i].ref + '_' + data[i].alt;
    if (label != undefined && label != '') {
      if (result != '') {
        result += ' ';
      }
      result += '<span onclick="openMutation(this)" data-mutation="' + id + '" data-clade="' + clade + '" class="mutation badge badge-' + (data[i].found ? 'success' : 'danger') + '" title="' + (data[i].found ? 'Found' : 'Not Found') + '">' + label + '</span>';
    }
  };
  return result;
}

function formatRemainingMutations(data, view, clade) {

  var result = '';
  for (var i = 0; i < data.length; i++) {
    var filtered = false;
    var label = view == 'aac' ? data[i].aac : data[i].nuc;
    var id = data[i].position + '_' + data[i].ref + '_' + data[i].alt;

    if (label != undefined && label != '') {
      if (result != '') {
        result += ' ';
      }
      result += '<span onclick="openMutation(this)" data-mutation="' + id + '" data-clade="' + clade + '" class="mutation badge ' + (data[i].type == 'hotspot' ? 'badge-hotspot' : (data[i].type == 'local private mutation' ? 'badge-local-private-mutation' : 'badge-global-private-mutation')) + '" title="' + data[i].type + '">' + label + '</span>';
    }
  };
  return result;
}

function formatRange(data) {

  var result = '';
  for (var i = 0; i < data.length; i++) {
    var label = data[i].trim();
    if (label != undefined && label != '') {
      if (result != '') {
        result += ' ';
      }
      result += '<span class="badge badge-secondary">' + label + '</span>';
    }
  };
  return result;
}

function formatHits(data) {

  var result = '<div class="collapse" id="collapseExample"><small><ol start="2">';
  for (var i = 0; i < data.otherClades.length; i++) {
    var label = '<a href="/phylogenies/{{job.phylotree}}/haplogroups/' + data.otherClades[i] + '" target="_blank">' + data.otherClades[i] + '</a>';
    result += '<li>' + label  + ' (' + data.otherQualities[i].toFixed(2) * 100 + '%)</li>';
  };
  result += '</ol></small></div>';
  return result;

}

function renderProgressBar(value) {
  var percentage = value * 100;
  return '<div class="progress" style="width: 60px;" title="Quality: ' + value.toFixed(2) + '">' +
    '<div class="progress-bar bg-success" role="progressbar" aria-valuenow="' + percentage + '" aria-valuemin="0" aria-valuemax="100" style="width: ' + percentage + '%">' +
    '<span class="sr-only">Quality: ' + value.toFixed(2) + '</span>' +
    '</div>' +
    '</div>';
}

function renderWarningsAndErrors(data) {
  var result = "";
  result += renderIssues(data.errors, "danger");
  result += renderIssues(data.warnings, "warning");
  result += renderIssues(data.infos, "info");
  return result;
}

function renderIssues(issues, type) {
  var result = "";
  for (var i = 0; i < issues.length; i++) {
    var label = issues[i].trim();
    result += '<small><div class="alert alert-' + type + '" role="alert">' + label + '</div></small>';

  }
  return result;
}

function renderIcon(data) {
  if (data.errors.length > 0) {
    return '<i class="fas fa-exclamation-circle text-danger"></i>';
  }
  if (data.warnings.length > 0) {
    return '<i class="fas fa-exclamation-triangle text-warning"></i>';
  }
  return '<i class="fas fa-check-circle text-success"></i>';
}


function renderText(data) {
  if (data.errors.length > 0) {
    return 'error';
  }
  if (data.warnings.length > 0) {
    return 'warning';
  }
  return 'ok';
}

//adjust column size on tab switch
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
  window.table.columns.adjust();
})
