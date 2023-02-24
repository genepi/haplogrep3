var recetUploadsString = localStorage.getItem('recent_uploads');
var recentUploads = [];
if (recetUploadsString != undefined) {
  recentUploads = JSON.parse(recetUploadsString );
}
var jobId = '{{job.getId()}}';
if (!recentUploads.includes(jobId)){
  recentUploads.push(jobId);
  localStorage.setItem('recent_uploads', JSON.stringify(recentUploads));
}
