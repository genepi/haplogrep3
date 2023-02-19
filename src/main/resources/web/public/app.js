function waitDialog() {
  bootbox.dialog({
    title: "Clade Classification",
    message: '<p>Uploading data</p><div class="progress"><div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 100%"><span class="sr-only">45% Complete</span></div></div>'
  });
}

//TODO: find correct parameter
bootbox.animate = false;

{{ include "../jobs/cards/haplogroups.js" }}
{{ include "../jobs/cards/samples.table.js" }}
{{ include "../jobs/cards/samples.details.js" }}

console.log("Loaded app.js");
