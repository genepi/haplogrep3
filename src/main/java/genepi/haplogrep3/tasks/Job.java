package genepi.haplogrep3.tasks;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import genepi.haplogrep3.model.AnnotatedSample;
import genepi.haplogrep3.model.Distance;
import genepi.haplogrep3.model.JobStatus;
import genepi.haplogrep3.model.Phylotree;
import genepi.haplogrep3.tasks.ExportReportTask.ExportDataFormat;
import genepi.io.FileUtil;
import util.ExportUtils;

public class Job implements Runnable {

	private String id;

	private int samples = 0;

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

	private Distance _distance;

	private boolean chip = false;

	private double hetLevel = 0.9;

	public static int EXPIRES_HOURS = 4;

	public String getId() {
		return id;
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

	public static Job create(String id, String workspace, Phylotree phylotree, List<File> files, Distance distance,
			boolean chip, double hetLevel) {
		Job job = new Job();
		job.setId(id);
		job.setStatus(JobStatus.SUBMITTED);
		job.setSubmittedOn(new Date());
		job.setExpiresOn(new Date(System.currentTimeMillis() + (EXPIRES_HOURS * 60 * 60 * 1000)));
		job.setPhylotree(phylotree.getId());
		job._workspace = workspace;
		job._phylotree = phylotree;
		job._files = files;
		job._distance = distance;
		job.chip = chip;
		job.hetLevel = hetLevel;
		job.save();
		return job;
	}

	public void run() {

		setStatus(JobStatus.RUNNING);
		save();

		String dataDirectory = FileUtil.path(_workspace, getId(), "data");

		try {

			ClassificationTask task = new ClassificationTask(_phylotree, _files, _distance);
			task.setChip(chip);
			task.setHetLevel(hetLevel);
			task.run();

			FileUtil.deleteDirectory(dataDirectory);

			if (task.isSuccess()) {

				String reportFilename = FileUtil.path(_workspace, getId(), "haplogroups.extended.csv");
				ExportReportTask exportReportTask = new ExportReportTask(task.getSamples(), reportFilename,
						ExportDataFormat.EXTENDED, _phylotree.getReference());
				exportReportTask.run();

				String extendedReportFilename = FileUtil.path(_workspace, getId(), "haplogroups.csv");
				ExportReportTask exportExtendedReportTask = new ExportReportTask(task.getSamples(),
						extendedReportFilename, ExportDataFormat.SIMPLE, _phylotree.getReference());
				exportExtendedReportTask.run();

				String seqqueneFilename = FileUtil.path(_workspace, getId(), "sequence");
				ExportSequenceTask exportSequenceTask = new ExportSequenceTask(task.getSamples(), seqqueneFilename,
						_phylotree.getReference());
				exportSequenceTask.run();

				save(task.getSamples());

				setSamples(task.getSamples().size());
				setExecutionTime(task.getExecutionTime());
				setFinisehdOn(new Date());
				setStatus(JobStatus.SUCCEDED);
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
			setError(e.getMessage());
			e.printStackTrace();
			save();

			FileUtil.deleteDirectory(dataDirectory);

		}
	}

	protected void save() {
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
