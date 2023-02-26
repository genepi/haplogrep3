package genepi.haplogrep3.tasks;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import genepi.haplogrep3.haplogrep.io.readers.StatisticCounter;
import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.HaplogroupStatistics;
import genepi.haplogrep3.model.JobStatus;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.tasks.ExportHaplogroupsTask.ExportDataFormat;
import genepi.haplogrep3.tasks.ExportSequenceTask.ExportSequenceFormat;
import genepi.io.FileUtil;

public class Job implements Runnable {

	private String id;

	private int samples = 0;

	private int samplesOk = 0;

	private int samplesWarning = 0;

	private int samplesError = 0;

	private String hash;

	private String phylotree;

	private Date submittedOn;

	private Date finisehdOn;

	private Date expiresOn;

	private long executionTime;

	private String error;

	private JobStatus status;

	private transient String _workspace;

	private transient Phylotree _phylotree;

	private transient List<File> _files;

	private Distance distance;

	private boolean chip = false;

	private double hetLevel = 0.9;

	private int hits = 20;

	private HaplogroupStatistics statistics;

	private List<StatisticCounter> counters;

	private boolean additionalOutput = false;

	public static long EXPIRES_HOURS = 24 * 7;

	private Job() {

	}

	public String getId() {
		return id;
	}

	public String getShortId() {
		return id.substring(0, 6);
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSamples() {
		return samples;
	}

	public void setSamples(int samples) {
		this.samples = samples;
	}

	public int getSamplesError() {
		return samplesError;
	}

	public void setSamplesError(int samplesError) {
		this.samplesError = samplesError;
	}

	public int getSamplesWarning() {
		return samplesWarning;
	}

	public void setSamplesWarning(int samplesWarning) {
		this.samplesWarning = samplesWarning;
	}

	public int getSamplesOk() {
		return samplesOk;
	}

	public void setSamplesOk(int samplesOk) {
		this.samplesOk = samplesOk;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPhylotree() {
		return phylotree;
	}

	public void setPhylotree(String phylotree) {
		this.phylotree = phylotree;
	}

	public void setChip(boolean chip) {
		this.chip = chip;
	}

	public boolean isChip() {
		return chip;
	}

	public void setHetLevel(double hetLevel) {
		this.hetLevel = hetLevel;
	}

	public double getHetLevel() {
		return hetLevel;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getHits() {
		return hits;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public Date getFinisehdOn() {
		return finisehdOn;
	}

	public void setFinisehdOn(Date finisehdOn) {
		this.finisehdOn = finisehdOn;
	}

	public Date getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	public long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public JobStatus getStatus() {
		return status;
	}

	public boolean isAdditionalOutput() {
		return additionalOutput;
	}

	public void setAdditionalOutput(boolean additionalOutput) {
		this.additionalOutput = additionalOutput;
	}

	public static Job create(String id, String workspace, Phylotree phylotree, List<File> files, Distance distance,
			boolean chip, double hetLevel, boolean additionalOutput) {
		Job job = new Job();
		job.setId(id);
		job.setStatus(JobStatus.SUBMITTED);
		job.setSubmittedOn(new Date());
		Date date = new Date(new Date().getTime() + (EXPIRES_HOURS * 60L * 60L * 1000L));
		job.setExpiresOn(date);
		job.setPhylotree(phylotree.getIdWithVersion());
		job._workspace = workspace;
		job._phylotree = phylotree;
		job._files = files;
		job.distance = distance;
		job.chip = chip;
		job.hetLevel = hetLevel;
		job.additionalOutput = additionalOutput;
		job.save();
		return job;
	}

	public void run() {

		setStatus(JobStatus.RUNNING);
		save();

		String dataDirectory = FileUtil.path(_workspace, getId(), "data");

		try {

			ClassificationTask task = new ClassificationTask(_phylotree, _files, distance);
			task.setChip(chip);
			task.setHetLevel(hetLevel);
			task.setHits(hits);
			task.setSkipAlignmentRules(false);
			task.run();

			FileUtil.deleteDirectory(dataDirectory);

			if (task.isSuccess()) {

				String extendedReportFilename = FileUtil.path(_workspace, getId(), "haplogroups.extended.csv");
				ExportHaplogroupsTask exportExtendedReportTask = new ExportHaplogroupsTask(task.getSamples(),
						extendedReportFilename, ExportDataFormat.EXTENDED, _phylotree.getReference());
				exportExtendedReportTask.run();

				String reportFilename = FileUtil.path(_workspace, getId(), "haplogroups.csv");
				ExportHaplogroupsTask exportReportTask = new ExportHaplogroupsTask(task.getSamples(), reportFilename,
						ExportDataFormat.SIMPLE, _phylotree.getReference());
				exportReportTask.run();

				if (additionalOutput) {

					String seqqueneFilename = FileUtil.path(_workspace, getId(), "sequence");
					ExportSequenceTask exportSequenceTask = new ExportSequenceTask(task.getSamples(), seqqueneFilename,
							ExportSequenceFormat.FASTA, _phylotree.getReference());
					exportSequenceTask.run();

					ExportSequenceTask exportSequenceMsaTask = new ExportSequenceTask(task.getSamples(),
							seqqueneFilename, ExportSequenceFormat.FASTA_MSA, _phylotree.getReference());
					exportSequenceMsaTask.run();

				}

				String qcReportFilename = FileUtil.path(_workspace, getId(), "samples");
				ExportQcReportTask exportQcReportTask = new ExportQcReportTask(task.getSamples(), qcReportFilename);
				exportQcReportTask.run();

				save(task.getSamples());

				setSamples(task.getSamples().size());
				setSamplesOk(task.getSamplesOk());
				setSamplesWarning(task.getSamplesWarning());
				setSamplesError(task.getSamplesError());
				setCounters(task.getCounters());

				statistics = new HaplogroupStatistics(task.getSamples(), _phylotree);

				setExecutionTime(task.getExecutionTime());
				setFinisehdOn(new Date());
				setStatus(JobStatus.SUCCEDED);

				// create html report and zip file with all needed files

				String htmlReportFilename = FileUtil.path(_workspace, getId(), "haplogroups.html");
				ExportHtmlReportTask htmlReportTask = new ExportHtmlReportTask(this, htmlReportFilename);
				htmlReportTask.run();

				String[] files = new String[] { extendedReportFilename, qcReportFilename + ".qc.txt",
						htmlReportFilename };

				String zipFilename = FileUtil.path(_workspace, getId(), "haplogroups.zip");
				CreateZipFileTask createZipFileTask = new CreateZipFileTask(files, zipFilename);
				createZipFileTask.run();

				save();

			} else {

				setExecutionTime(task.getExecutionTime());
				setFinisehdOn(new Date());
				setStatus(JobStatus.FAILED);
				setError(task.getError());
				save();

			}

		} catch (Exception e) {

			setExecutionTime(0);
			setFinisehdOn(new Date());
			setStatus(JobStatus.FAILED);
			e.printStackTrace();
			save();

			FileUtil.deleteDirectory(dataDirectory);

		}
	}

	public HaplogroupStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(HaplogroupStatistics statistics) {
		this.statistics = statistics;
	}

	public void setCounters(List<StatisticCounter> counters) {
		this.counters = counters;
	}

	public List<StatisticCounter> getCounters() {
		return counters;
	}

	protected synchronized void save() {
		String jobFilename = FileUtil.path(_workspace, getId() + ".json");
		Gson gson = new Gson();
		FileUtil.writeStringBufferToFile(jobFilename, new StringBuffer(gson.toJson(this)));
	}

	protected void save(List<AnnotatedSample> samples) {
		String cladesFilename = FileUtil.path(_workspace, getId(), "clades.json");
		Gson gson = new Gson();
		FileUtil.writeStringBufferToFile(cladesFilename, new StringBuffer(gson.toJson(samples)));
	}

}
