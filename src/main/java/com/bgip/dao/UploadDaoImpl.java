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
import com.bgip.model.upload.FoldersBean;
import com.bgip.mongo.MongoManager;

@Component
public class UploadDaoImpl extends BaseDAO implements UploadDAO {

	@Autowired
	MongoManager mongoManager;

	@Autowired
	BgipConstants bgipConstants;

	@Autowired
	StatusCodes statusCodes;

	public List<FolderResponse> uploadedFiles(List<FolderResponse> folderList, String loginUser) throws Exception {
		List<FolderResponse> list = new ArrayList<FolderResponse>();
		// for (FolderResponse folder : folderList) {
		// folder.setUserName(loginUser);
		// list.add(createEmptyFolder(folder));
		// }

		if (CollectionUtils.isNotEmpty(folderList)) {
			for (FolderResponse folderRes : folderList) {
				list.add(createEmptyFolder(folderRes, loginUser));
			}
		}

		return list;
	}

	public FolderResponse createFolders(FolderResponse folder) throws Exception {
		FolderResponse result = null;
		List<FilesBean> fileList = new ArrayList<FilesBean>();
		FoldersBean folderBean = new FoldersBean();

		folderBean.setCreated(folder.getCreated());
		folderBean.setLink(folder.getLink());
		if (CommonUtils.isEmpty(folder.getParentId())) {
			folderBean.setParentId("0");
			folderBean.setFolderName(Long.toString(System.currentTimeMillis()));
		} else {
			folderBean.setParentId(folder.getParentId());
			folderBean.setFolder(true);
			folderBean.setFolderName(folder.getFolderName());
		}
		folderBean.setUserName(folder.getUserName());

		FoldersBean folderBean2 = (FoldersBean) insertDB(com.bgip.constants.BgipConstants.FOLDER_COLLECTION,
				folderBean);
		System.out.println("  Result folderBean2 : " + folderBean2.toString());

		if (CollectionUtils.isNotEmpty(folder.getFiles())) {
			for (FilesBean file : folder.getFiles()) {
				file.setFolderId(folderBean2.getId());
				// FilesBean file1 = ;
				fileList.add(createFile(file));
			}
		}
		result = mongoManager.getObjectByField(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "id",
				folderBean2.getId(), FolderResponse.class);
		result.setFiles(fileList);
		return result;
	}

	// Create File by providing FolderId
	public FilesBean createFile(FilesBean file) throws Exception {
		FilesBean fileFromDB = null;
		if (file != null) {
			fileFromDB = (FilesBean) insertDB(com.bgip.constants.BgipConstants.FILES_COLLECTION, file);
		}
		return fileFromDB;
	}

	public FolderResponse createEmptyFolder(FolderResponse emptyFolder, String loginUser) throws Exception {
		FoldersBean folder = new FoldersBean();
		List<FilesBean> fileList = new ArrayList<FilesBean>();
		FolderResponse result = null;
		FoldersBean folderBean2 = null;
		folder.setUserName(loginUser);
		if (CommonUtils.isEmpty(emptyFolder.getParentId())) {
			folder.setParentId("0");
		} else {
			folder.setParentId(emptyFolder.getParentId());
			folder.setFolder(true);
		}
		if (CommonUtils.isEmpty(emptyFolder.getFolderName())) {
			folder.setFolderName(Long.toString(System.currentTimeMillis()));
		} else {
			folder.setFolderName(emptyFolder.getFolderName());
		}
		if (CommonUtils.isEmpty(emptyFolder.getLink())) {
			folder.setLink("");
		} else {
			folder.setLink(emptyFolder.getLink());
		}
		try {
			folderBean2 = (FoldersBean) insertDB(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, folder);

			if (CollectionUtils.isNotEmpty(emptyFolder.getFiles())) {
				for (FilesBean file : emptyFolder.getFiles()) {
					file.setFolderId(folderBean2.getId());
					file.setUserName(folderBean2.getUserName());
					;
					fileList.add(createFile(file));
				}
			}
			result = mongoManager.getObjectByField(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "id",
					folderBean2.getId(), FolderResponse.class);
			result.setFiles(fileList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<FilesBean> getFilesByFolderId(String folderId, String loginUser) throws Exception {
		System.out.println("folerId : " + folderId);
		System.out
				.println("result : " + mongoManager.getObjectsByField(com.bgip.constants.BgipConstants.FILES_COLLECTION,
						"folderId", folderId, FilesBean.class));
		// return
		// mongoManager.getObjectsByField(com.bgip.constants.BgipConstants.FILES_COLLECTION,
		// "folderId", folderId, FilesBean.class);
		return mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FILES_COLLECTION, "folderId", folderId,
				"userName", loginUser, FilesBean.class);
	}

	public UploadRequest getAllFiles(String loginUser) throws Exception {

		UploadRequest finalResult = new UploadRequest();
		List<FolderResponse> allFiles = mongoManager.getObjectsByField(
				com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "userName", loginUser, FolderResponse.class);

		if (CollectionUtils.isNotEmpty(allFiles)) {
			for (FolderResponse folder : allFiles) {
				folder.setFiles(getFilesByFolderId(folder.getId(), loginUser));
			}
		}
		finalResult.setFolderList(allFiles);

		return finalResult;
	}

	@Override
	public ResponseBean makeFavouriteFolder(String folderId, String loginUser) throws Exception {
		ResponseBean response = new ResponseBean();
		if (CommonUtils.isNotEmpty(folderId)) {

			FolderResponse favFolder1 = mongoManager.findBy3Fields(com.bgip.constants.BgipConstants.FOLDER_COLLECTION,
					"id", folderId, "userName", loginUser, "favourite", false, FolderResponse.class);
			if (favFolder1 != null) {
				mongoManager.updateByField(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "_id",
						new ObjectId(folderId), "favourite", true);
				response.setMessage(StatusCodes.FAVOURITE_MESSAGE);
			} else {
				mongoManager.updateByField(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, "_id",
						new ObjectId(folderId), "favourite", false);
				response.setMessage(StatusCodes.UNFAVOURITE_MESSAGE);
			}

		}
		return response;
		// mongoManager.getObjectByID(com.bgip.constants.BgipConstants.FOLDER_COLLECTION,
	}
	
	
	@Override
	public ResponseBean makeFavouriteFile(String fileId, String loginUser) throws Exception {
		ResponseBean response = new ResponseBean();
		System.out.println("fileId : "+fileId+" login user :"+loginUser);
		System.out.println("makeFavouriteFile List 3: "+System.currentTimeMillis());
		if (CommonUtils.isNotEmpty(fileId)) {

			FilesBean fileFromDB = mongoManager.findBy3Fields(com.bgip.constants.BgipConstants.FILES_COLLECTION,
					"id", fileId, "userName", loginUser, "favourite", false, FilesBean.class);
			System.out.println("makeFavouriteFile List 4: "+System.currentTimeMillis());

			
			if (fileFromDB != null) {
				System.out.println("makeFavouriteFile List 5: "+System.currentTimeMillis());

				mongoManager.updateByField(com.bgip.constants.BgipConstants.FILES_COLLECTION, "_id",
						new ObjectId(fileId), "favourite", true);
				response.setMessage(StatusCodes.FAVOURITE_MESSAGE);
			} else {
				mongoManager.updateByField(com.bgip.constants.BgipConstants.FILES_COLLECTION, "_id",
						new ObjectId(fileId), "favourite", false);
				response.setMessage(StatusCodes.UNFAVOURITE_MESSAGE);
			}
		}
		return response;
		// mongoManager.getObjectByID(com.bgip.constants.BgipConstants.FOLDER_COLLECTION,
	}

	
	
	@Override
	public List<FolderResponse> getFavouriteFolders(String loginUser) throws Exception {
		List<FolderResponse> favFolderList = null;
		System.out.println("folder List 3: "+System.currentTimeMillis());
		favFolderList = mongoManager.getObjectsBy2Fields(com.bgip.constants.BgipConstants.FOLDER_COLLECTION, 
				"userName", loginUser, "favourite", true, FolderResponse.class);
		System.out.println("folder List 4: "+System.currentTimeMillis());
		if( favFolderList == null ) {
			throw new BgipException(StatusCodes.NOT_FOUND, "You don't have favourite Foulder List");
		}
		
		return favFolderList;
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

	
	
	
	


	public void downloadFiles(FoldersBean files) throws Exception {

	}

	@Override
	public FilesBean getFileById(String fileId, String loginUser) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
