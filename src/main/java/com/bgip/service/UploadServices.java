package com.bgip.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bgip.dao.UploadDAO;
import com.bgip.model.upload.FolderResponse;
import com.bgip.model.upload.UploadRequest;
import com.bgip.model.ResponseBean;
import com.bgip.model.upload.FilesBean;
import com.bgip.model.upload.FoldersBean;

@Service
public class UploadServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServices.class);

	@Autowired
	UploadDAO uploadDao;

	public List<FolderResponse> uploadedFiles(List<FolderResponse> folders, String loginUser) throws Exception {
		LOGGER.info("BGIP  uploadedFiles  method call :: ");
		return uploadDao.uploadedFiles(folders, loginUser);
	}

	public FolderResponse createEmptyFolder(FolderResponse emptyFolder, String loginUser) throws Exception {

		return uploadDao.createEmptyFolder(emptyFolder, loginUser);
	}

	public List<FilesBean> getFilesByFolderId(String folderId, String loginUser) throws Exception {

		return uploadDao.getFilesByFolderId(folderId, loginUser);
	}
	
	public FilesBean getFilbyId(String folderId, String loginUser) throws Exception {

		return uploadDao.getFileById(folderId, loginUser);
	}

	public UploadRequest getAllFiles(String loginUser) throws Exception {
		LOGGER.info("BGIP  getAllFiles  method call :: ");
		return uploadDao.getAllFiles(loginUser);
	}


	public ResponseBean makeFavouriteFolder( String folderId, String loginUser) throws Exception{

	return uploadDao.makeFavouriteFolder(folderId, loginUser);
}
	
	public ResponseBean makeFavouriteFile( String fileId, String loginUser) throws Exception{
		System.out.println("files List 2: "+System.currentTimeMillis());
		return uploadDao.makeFavouriteFile(fileId, loginUser);
	}
	
	public List<FolderResponse> getFavouriteFolders( String loginUser) throws Exception{
		System.out.println("folder List 2: "+System.currentTimeMillis());
		return uploadDao.getFavouriteFolders(loginUser);
	}
	
	public List<FilesBean> getFavouriteFiles( String loginUser) throws Exception{
		System.out.println("files List 2: "+System.currentTimeMillis());
		return uploadDao.getFavouriteFiles(loginUser);
	}
	
	
	
	
	
	
	
	public void downloadFiles(FoldersBean files) throws Exception {
		LOGGER.info("BGIP  downloadFiles  method call :: ");
	}


	public void getAllFilesByFolderId(FoldersBean files) throws Exception {
		LOGGER.info("BGIP  getAllFilesByFolderId  method call :: ");
	}

	public void getFiles(FoldersBean files) throws Exception {
		LOGGER.info("BGIP  getFiles  method call :: ");
	}
	
}
