class SamplesDetails {

  phylotree;

  selectedMutation = undefined;

  constructor(phylotree, data, serverUrl) {
    this.phylotree = phylotree;
    this.data = data;
    this.serverUrl = serverUrl;
  }

  show() {

    var self = this;
    var dialog = bootbox.dialog({
      title: self.renderIcon(self.data) + '&nbsp;' + self.data.sample,
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
        '  <div class="row">' +
        '    <div class="col-md-8">' +
        '      <div class="card" style="padding-right: 0px;">' +
        '        <div style="height: 500px; overflow-y: scroll; padding: 10px;">' +
        '          ' + self.formatDetails() +
        '        </div>' +
        '      </div>' +
        '    </div>' +
        '    <div class="col-md-4">' +
        '      <div class="card" style="padding-left:0px;padding-right: 0px;">' +
        '        ' + self.renderMutationDetails() +
        '      </div>' +
        '    </div>' +
        '  </div>' +
        '</div>'
    });

    dialog.find("div.modal-dialog").addClass("scroll-modal-body-horizontal");

    $(".mutation").on("click", function() {
      self.showMutationDetails(this, self.serverUrl);
    });

  }


  showMutationDetails(source, server) {
    if (this.selectedMutation) {
      $(this.selectedMutation).removeClass("selected-mutation");
    }
    this.selectedMutation = source;

    $(source).addClass("selected-mutation");
    var mutation = $(source).data("mutation");
    var clade = $(source).data("clade");
    var url = server + '/phylogenies/' + this.phylotree + '/haplogroups/' + clade + '/mutations/' + mutation + '?minimal=true';
    $("#details").attr('src', url);
  }

  renderMutationDetails() {
    return '<iframe id="details" width="100%" height="500px" frameborder="0"></iframe>';
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

  formatDetails() {

    var topHitUrl = '/phylogenies/' + this.phylotree + '/haplogroups/' + this.data.clade;
    var topHit = '' +
      '<b>Top Hit</b><br>' +
      '<b><a href="' + topHitUrl + '" target="_blank">' + this.data.clade + '</a></b>' +
      ' (' + this.data.quality.toFixed(2) * 100 + '%)' +
      '<br><br>';

    var expectedMutation = '' +
      '<b>Expected Mutations</b>&nbsp; ' +
      '<a href="https://haplogrep.readthedocs.io/en/latest/mutations/#expected-mutations" target="_blank" title="Help" class="col-form-label"> ' +
      '  <i class="fas fa-question-circle"></i> ' +
      '</a><br>' +
      this.formatExpectedMutations() +
      '<br><br>';

    var remainingMutations = '' +
      '<b>Remaining Mutations</b>&nbsp; ' +
      '<a href="https://haplogrep.readthedocs.io/en/latest/mutations/#remaining-mutations" target="_blank" title="Help" class="col-form-label"> ' +
      '  <i class="fas fa-question-circle"></i> ' +
      '</a><br>' +
      this.formatRemainingMutations() +
      '<br><br>';

    var additionalHits = '' +
      '<b><a data-toggle="collapse" href="#ranges" role="button" aria-expanded="false" aria-controls="ranges">Additional Hits</a></b><br>' +
      this.formatHits() +
      ' <br>';

    var ranges = '' +
      '<b>Ranges</b><br>' +
      this.formatRange() +
      '<br><br>';

    return this.renderWarningsAndErrors() +
      topHit +
      expectedMutation +
      remainingMutations +
      additionalHits +
      ranges;

  }

  formatExpectedMutations() {

    var result = '';
    for (var i = 0; i < this.data.expectedMutations.length; i++) {
      var mutation = this.data.expectedMutations[i];
      var label = mutation.nuc;
      var found = mutation.found;
      var id = mutation.position + '_' + mutation.ref + '_' + mutation.alt;

      if (result != '') {
        result += ' ';
      }
      result += '<span data-mutation="' + id + '" data-clade="' + this.data.clade + '" class="mutation badge badge-' + (found ? 'success' : 'danger') + '" title="' + (found ? 'Found' : 'Not Found') + '">' +
        label +
        '</span>';

    };
    return result;
  }

  formatRemainingMutations() {

    var result = '';
    for (var i = 0; i < this.data.remainingMutations.length; i++) {
      var mutation = this.data.remainingMutations[i];
      var label = mutation.nuc;
      var type = mutation.type;
      var id = mutation.position + '_' + mutation.ref + '_' + mutation.alt;
      if (result != '') {
        result += ' ';
      }
      result += '<span data-mutation="' + id + '" data-clade="' + this.data.clade + '" class="mutation badge ' + (type == 'hotspot' ? 'badge-hotspot' : (type == 'local private mutation' ? 'badge-local-private-mutation' : 'badge-global-private-mutation')) + '" title="' + type + '">' + label + '</span>';

    };
    return result;
  }

  formatRange() {

    var result = '';
    for (var i = 0; i < this.data.ranges.length; i++) {
      var label = this.data.ranges[i].trim();
      if (label != undefined && label != '') {
        if (result != '') {
          result += ' ';
        }
        result += '<span class="badge badge-secondary">' + label + '</span>';
      }
    };
    return result;
  }

  formatHits() {

    var result = '<div class="collapse" id="ranges">' +
      '<small>' +
      '<ol start="2">';
    for (var i = 0; i < this.data.otherClades.length; i++) {
      var hit = this.data.otherClades[i];
      var quality = this.data.otherQualities[i];
      var url = '/phylogenies/' + this.phylotree + '/haplogroups/' + hit;
      var link = '<a href="' + url + '" target="_blank">' + hit + '</a>';
      result += '<li>' + link + ' (' + quality.toFixed(2) * 100 + '%)</li>';
    };
    result += '</ol>' +
      '</small>' +
      '</div>';
    return result;

  }

  renderWarningsAndErrors() {
    var result = "";
    result += this.renderIssues(this.data.errors, "danger");
    result += this.renderIssues(this.data.warnings, "warning");
    result += this.renderIssues(this.data.infos, "info");
    return result;
  }

  renderIssues(issues, type) {
    var result = "";
    for (var i = 0; i < issues.length; i++) {
      var label = issues[i].trim();
      result += '<small><div class="alert alert-' + type + '" role="alert">' + label + '</div></small>';

    }
    return result;
  }

}
