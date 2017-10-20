package com.bgip.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.bgip.model.upload.FolderResponse;
import com.bgip.model.upload.UploadRequest;
import com.bgip.model.ResponseBean;
import com.bgip.model.upload.FilesBean;
import com.bgip.model.upload.FolderRequest;
import com.bgip.model.upload.FolderBean;

@Transactional
public interface UploadDAO {

	public FolderRequest uploadedFiles(FolderRequest folder, String loginUser) throws Exception;
	
	public FolderBean createEmptyFolder(FolderBean emptyFolder, String loginUser)throws Exception;
	
	public FolderResponse getFolderDetails(String folderId, String loginUser) throws Exception;
	
	public FilesBean getFileById(String fileId, String loginUser) throws Exception;


	public FolderResponse getAllFiles(String loginUser) throws Exception;
	
	public ResponseBean makeFavouriteFolder( String folderId, String loginUser) throws Exception;
	
	public ResponseBean makeFavouriteFile( String fileId, String loginUser) throws Exception;
	
	public List<FolderResponse> getFavouriteFolders( String loginUser) throws Exception;
	
	public List<FilesBean> getFavouriteFiles( String loginUser) throws Exception;
	
	
	public void downloadFiles(FolderBean files) throws Exception;
	
}
