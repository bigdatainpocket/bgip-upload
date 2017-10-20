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
import com.bgip.model.upload.FolderRequest;
import com.bgip.model.upload.FolderBean;

@Service
public class UploadServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServices.class);

	@Autowired
	UploadDAO uploadDao;

	public FolderRequest uploadFolder(FolderRequest folder, String loginUser) throws Exception {
		LOGGER.info("BGIP  uploadedFiles  method call :: ");
		return uploadDao.uploadedFiles(folder, loginUser);
	}

	public FolderBean createEmptyFolder(FolderBean emptyFolder, String loginUser) throws Exception {

		return uploadDao.createEmptyFolder(emptyFolder, loginUser);
	}

	public FolderResponse getFolderDetails(String folderId, String loginUser) throws Exception {

		return uploadDao.getFolderDetails(folderId, loginUser);
	}
	
	public FilesBean getFilbyId(String folderId, String loginUser) throws Exception {

		return uploadDao.getFileById(folderId, loginUser);
	}

	public FolderResponse getAllFiles(String loginUser) throws Exception {
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
	
	
	
	
	
	
	
	public void downloadFiles(FolderBean files) throws Exception {
		LOGGER.info("BGIP  downloadFiles  method call :: ");
	}


	public void getAllFilesByFolderId(FolderBean files) throws Exception {
		LOGGER.info("BGIP  getAllFilesByFolderId  method call :: ");
	}

	public void getFiles(FolderBean files) throws Exception {
		LOGGER.info("BGIP  getFiles  method call :: ");
	}
	
}
