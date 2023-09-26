package com.springboot.ecommerce.pojo;

public class ImageDTO {
	private String fileContentBase64;
	private String fileName;
	private int userId;
	private int newsId;

	private String heading;
	private String tag;
	private String[] categoryId;
	private String desc;

	private String videoLink;
	private String title;
	private String description;
	private String keywords;
	private String altTag;
	private String imageName;
	private String url;

	private String[] multiImages;

	public String getFileContentBase64() {
		return fileContentBase64;
	}

	public void setFileContentBase64(String fileContentBase64) {
		this.fileContentBase64 = fileContentBase64;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String[] getMultiImages() {
		return multiImages;
	}

	public void setMultiImages(String[] multiImages) {
		this.multiImages = multiImages;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String[] getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String[] categoryId) {
		this.categoryId = categoryId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAltTag() {
		return altTag;
	}

	public void setAltTag(String altTag) {
		this.altTag = altTag;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

}