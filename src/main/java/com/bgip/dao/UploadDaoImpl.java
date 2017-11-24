package com.bgip.dao;

import org.jasypt.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;
import com.bgip.constants.BgipConstants;
import com.bgip.constants.StatusCodes;
import com.bgip.exception.BgipException;
import com.bgip.model.upload.FolderResponse;
import com.bgip.model.upload.UploadRequest;
import com.bgip.model.ResponseBean;
import com.bgip.model.upload.FilesBean;
import com.bgip.model.upload.FolderRequest;
import com.bgip.model.upload.FolderBean;
import com.bgip.mongo.MongoManager;

@Component
public class UploadDaoImpl extends BaseDAO implements UploadDAO {

	@Autowired
	MongoManager mongoManager;

	@Autowired
	BgipConstants bgipConstants;

	@Autowired
	StatusCodes statusCodes;

	public FolderRequest uploadedFiles(FolderRequest folderRequest, String loginUser) throws BgipException {
		
		folderRequest.setUserName(loginUser);
		FolderRequest finalResult = new FolderRequest();
		List<FilesBean> fileList = new ArrayList<FilesBean>();

		
	
		
		
		
		if( CommonUtils.isNotEmpty(folderRequest.getFolderName())) {
			finalResult = createFolder(folderRequest);
		}else {
			
			if( !folderRequest.getParentFolderId().equals("0")  ) {
				if( CollectionUtils.isNotEmpty(folderRequest.getFileList())) {
					for( FilesBean file : folderRequest.getFileList()) {
						file.setUserName(loginUser);
						file.setFolderId(folderRequest.getParentFolderId());
						try {
							fileList.add(createFile(file));
							finalResult.setFileList(fileList);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				
				
			
			}else {
				
				if( CollectionUtils.isNotEmpty(folderRequest.getFileList())) {
//					finalResult.setParentFolderId(null);
					for( FilesBean file : folderRequest.getFileList()) {
						file.setUserName(loginUser);
						try {
//							return fileList.add(createFile(file));
							fileList.add(createFile(file));
							finalResult.setFileList(fileList);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}
			
		
//		
//		if( CommonUtils.isNotEmpty(folderRequest.getFolderName())) {
//			finalResult = createFolder(folderRequest);
//		}else {
//			
//			if( !folderRequest.getParentFolderId().equals("0")  ) {
//				finalResult =  createFolder(folderRequest);
//			
//			}else {
//				
//				if( CollectionUtils.isNotEmpty(folderRequest.getFileList())) {
////					finalResult.setParentFolderId(null);
//					for( FilesBean file : folderRequest.getFileList()) {
//						file.setUserName(loginUser);
//						try {
////							return fileList.add(createFile(file));
//							fileList.add(createFile(file));
//							finalResult.setFileList(fileList);
//							
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			
//		}
//		
//		
//		if( CommonUtils.isNotEmpty(folderRequest.getFolderName())) {
//			System.out.println(" folderRequest.getFolderName() : ");
//			finalResult = createFolder(folderRequest);
//		}else {
//			System.out.println(" folderRequest.getFolderName() else part: ");
//			
//			System.out.println(" folderRequest.getParentFolderId(): "+folderRequest.getParentFolderId());
//			
//			if( CommonUtils.isEmpty(folderRequest.getParentFolderId()) || folderRequest.getParentFolderId() == 0) {
//				System.out.println(" folderRequest.getParentFolderId() : ");
//				finalResult.setFolderName("untitled_folder");
//				finalResult = createFolder(folderRequest);
//			}else {
//				
//				System.out.println(" folderRequest.getParentFolderId() else part: ");
//				
//				if( CollectionUtils.isNotEmpty(folderRequest.getFileList())) {
//					System.out.println(" folderRequest.getFileList()");
////					finalResult.setFolderName("untitled_folder");
////					finalResult.setParentFolderId("0");
////					finalResult = createFolder(folderRequest);
//					
//				}else {
//					throw new BgipException(StatusCodes.NOT_FOUND, " Error in File Upload Api !! Please Upload valid File/Folder  ");
//				}
//			}
//		}
		return finalResult;
	}
	
	
	
	public FilesBean createFile(FilesBean file) throws Exception {
		FilesBean fileFromDB = new FilesBean();
		if (file != null) {
			if(CommonUtils.isEmpty(file.getFolderId()) ) {
				file.setFolderId("0");
			}
			fileFromDB = (FilesBean) insertDB(com.bgip.constants.BgipConstants.FILES_COLLECTION, file);
		}
		return fileFromDB;
	}


	
	
	public FolderRequest createFolder( FolderRequest folderRequest) throws BgipException{
		
		List<FilesBean> files = new ArrayList<FilesBean>();
		FolderBean folder = new FolderBean();
		
		System.out.println(" folderRequest parentId : "+folderRequest.getParentFolderId());
		
		if( CommonUtils.isEmpty(folderRequest.getParentFolderId())) {
			folder.setParentFolderId("0");
		}else {
			folder.setParentFolderId(folderRequest.getParentFolderId());
		}
		folder.setUserName(folderRequest.getUserName());
		folder.setFolderName(folderRequest.getFolderName());
		if( CommonUtils.isNotEmpty(folderRequest.getLink())) {
			folder.setLink(folderRequest.getLink());
		}
		folder.setCreated(folderRequest.getCreated());
		
		
		FolderBean folderFromDB = new FolderBean();
		try {
			folderFromDB = (FolderBean) insertDB(com.bgip.constants.BgipConstants.FOLDER_COLLECTION,	folder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if( CollectionUtils.isNotEmpty(folderRequest.getFileList())) {
			for( FilesBean file : folderRequest.getFileList() ) {
				file.setFolderId(folderFromDB.getId());
				file.setUserName(folderFromDB.getUserName());
				try {
					files.add((FilesBean) insertDB(com.bgip.constants.BgipConstants.FILES_COLLECTION, file));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		FolderRequest result = new FolderRequest();
		try {
			result = mongoManager.getObjectByID(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, folderFromDB.getId(), FolderRequest.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.setFileList(files);
		
		
		return result;
	}
	
	
	
	public FolderResponse getAllFiles(String loginUser) throws Exception {
		FolderResponse FolderFromDB = new FolderResponse();
		FolderFromDB.setUserName(loginUser);
		FolderFromDB.setFolderList(getSubFolderList("0",loginUser ));
		FolderFromDB.setFileList(getFilesByFolderId("0",loginUser ));
		
		return FolderFromDB;
	}
	
	public List<FolderBean> getSubFolderList(String folderId, String loginUser) throws Exception {
		List<FolderBean> folderList = new ArrayList<FolderBean>();
		try {
			folderList = mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FOLDER_COLLECTION,
					"parentFolderId", folderId, "userName", loginUser, FolderBean.class);
			System.out.println("  folderId : "+folderId);
			System.out.println(" Foldr list : "+folderList);
			
//			folderList = mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "parentFolderId", folderId,
//					"userName", loginUser, FolderBean.class);
			return folderList;
		}catch (Exception e) {
			e.printStackTrace();
			throw new BgipException(StatusCodes.NOT_FOUND, " Error in getSubFolderList Api !! file list doesn't exist ");
		}
		
	}


	
	
	
	public FolderResponse getAllDriveFolders(String loginUser) throws Exception {
		FolderResponse FolderFromDB = new FolderResponse();
		FolderFromDB.setUserName(loginUser);
		FolderFromDB.setFolderList(getSubFolderList("0",loginUser ));
		return FolderFromDB;
	}
	
	
	
	
	public FolderResponse getFolderDetails(String folderId, String loginUser) throws Exception {
		FolderResponse FolderFromDB = new FolderResponse();
		if( folderId.equals("0")) {
			FolderFromDB = getAllDriveFolders(loginUser);
		}else {
			 FolderFromDB = mongoManager.getObjectByField(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "id", folderId, FolderResponse.class);
				
				if( FolderFromDB ==null ) {
					throw new BgipException(StatusCodes.NOT_FOUND, "  Error in getFolderDetails Api !! FolderId doesn't exist ");
				}
				FolderFromDB.setFolderList(getSubFolderList(folderId,loginUser ));
				FolderFromDB.setFileList(getFilesByFolderId(folderId,loginUser ));
		}
		
		return FolderFromDB;
	}
	
	
	@Override
	public FolderResponse getFavouriteFolders(String loginUser) throws Exception {
		FolderResponse finalResult = new FolderResponse();
		
		List<FilesBean> fileList = mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FILES_COLLECTION, "userName", loginUser,
				"favourite", true, FilesBean.class);
		List<FolderBean> folderList = mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "userName", loginUser,
				"favourite", true, FolderBean.class);
		if( fileList != null) {
			finalResult.setFolderList(folderList);
		}
		if( folderList != null) {
			finalResult.setFileList(fileList);
		}
		return finalResult;
	}
	
	
	
	
	
	
	
	
	public List<FilesBean> getFilesByFolderId(String folderId, String loginUser) throws Exception {
		try {
			return mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FILES_COLLECTION, "folderId", folderId,
					"userName", loginUser, FilesBean.class);
		}catch (Exception e) {
			e.printStackTrace();
			throw new BgipException(StatusCodes.NOT_FOUND, " Error in getFilesByFolderId Api !! file list doesn't exist ");
		}
	}
	

	public FolderBean createEmptyFolder(FolderBean emptyFolder, String loginUser) throws Exception {
		FolderBean folderFromDB = new FolderBean();
		if( CommonUtils.isEmpty(emptyFolder.getFolderName())) {
			emptyFolder.setFolderName("untitled_folder");
		}
		if( CommonUtils.isEmpty(emptyFolder.getParentFolderId())) {
			emptyFolder.setParentFolderId("0");
		}
		try {
			folderFromDB = (FolderBean) insertDB(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, emptyFolder);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderFromDB;
	}

	


	@Override
	public ResponseBean makeFavouriteFolder(String folderId, String loginUser) throws Exception {
		ResponseBean response = null;
		if (CommonUtils.isNotEmpty(folderId)) {
			FolderBean favFolderFromdDB= mongoManager.getObjectByID(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, folderId, FolderBean.class);
			try {
				if( favFolderFromdDB != null) {
					
					if( favFolderFromdDB.isFavourite() == false) {
						mongoManager.updateByObjectId(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "_id",
								new ObjectId(folderId), "favourite", true);
						 response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE, " Favorite Success");
					}else {
						mongoManager.updateByObjectId(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "_id",
								new ObjectId(folderId), "favourite", false);
						 response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE, " Unfavorite Success");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BgipException(StatusCodes.NOT_FOUND, " Folder ID Not Found ");
			}
		}else {
			throw new BgipException(StatusCodes.NOT_FOUND, " folderId must not be null/empty ");
		}
		return response;
	}
	
	
	@Override
	public ResponseBean makeFavouriteFile(String fileId, String loginUser) throws Exception {
		ResponseBean response = null;
		if (CommonUtils.isNotEmpty(fileId)) {
			FilesBean favFolderFromdDB= mongoManager.getObjectByID(com.bgip.constants.BgipConstants.FILES_COLLECTION, fileId, FilesBean.class);
			try {
				if( favFolderFromdDB != null) {
					
					if( favFolderFromdDB.isFavourite() == false) {
						mongoManager.updateByObjectId(com.bgip.constants.BgipConstants.FILES_COLLECTION, "_id",
								new ObjectId(fileId), "favourite", true);
						 response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE, " Favorite Success");
					}else {
						mongoManager.updateByObjectId(com.bgip.constants.BgipConstants.FILES_COLLECTION, "_id",
								new ObjectId(fileId), "favourite", false);
						 response = new ResponseBean(StatusCodes.SUCCESS_MESSAGE, " Unfavorite Success");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BgipException(StatusCodes.NOT_FOUND, " File ID Not Found ");
			}
		}else {
			throw new BgipException(StatusCodes.NOT_FOUND, " File ID must not be null/empty ");
		}
		
		
		return response;
	}

	

	
	@Override
	public List<FilesBean> getFavouriteFiles(String loginUser) throws Exception {
		List<FilesBean> favFolderList = null;
		System.out.println("files List 3: "+System.currentTimeMillis());
		favFolderList = mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FILES_COLLECTION, 
				"userName", loginUser, "favourite", true, FilesBean.class);
		System.out.println("files List 4: "+System.currentTimeMillis());

		if( favFolderList == null ) {
			throw new BgipException(StatusCodes.NOT_FOUND, "You don't have favourite File List");
		}
		
		return favFolderList;
	}

	


	public void downloadFiles(FolderBean files) throws Exception {

	}

	@Override
	public FilesBean getFileById(String fileId, String loginUser) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
