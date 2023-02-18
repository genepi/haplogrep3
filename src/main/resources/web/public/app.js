function waitDialog() {
  bootbox.dialog({
    title: "Clade Classification",
    message: '<p>Uploading data</p><div class="progress"><div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 100%"><span class="sr-only">45% Complete</span></div></div>'
  });
}

console.log("Loaded app.js");
