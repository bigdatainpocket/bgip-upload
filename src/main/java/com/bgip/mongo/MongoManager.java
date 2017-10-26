package com.bgip.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import com.bgip.constants.BgipConstants;
import com.bgip.model.user.UserBean;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

@Component
public class MongoManager { 
	
	@Autowired
	BgipConstants BgipConstants;
	
	@Autowired 
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private Mongo mongo;
	private static final String DB_NAME= "bgip";
	
	public  <T> T findBy2Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, Class<T> className) throws Exception {
		T object = null;
		try {
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
			object = mongoTemplate.findOne(query, className, collectionName);
			return object;
				} catch (Exception e) {
					throw new Exception(e);
			}
		}
		
	
	public  List<UserBean> searchLikeQuery(String keyword) throws Exception {
		List<UserBean> objectList = null;
		Query query = new Query();
		query.limit(10);	
		
		new Criteria();
		Criteria criteria1 = Criteria.where("firstName").regex(keyword, "i");
		new Criteria();
		Criteria criteria2 = Criteria.where("lastName").regex(keyword, "i");
		query.addCriteria(new Criteria().orOperator(criteria1,criteria2));
		
		objectList = mongoTemplate.find(query, UserBean.class, BgipConstants.USER_COLLECTION);
		
		return objectList;
	}
	
	
	
	
		
	public <T>long getCountByField(String collectionName, String fieldName1, String fieldValue1,String fieldName2, String fieldValue2, Class<T> className) {
		   Query query =  new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
		  return mongoTemplate.count(query, className, collectionName);
		 }
	
	public <T>long getCountByField(String collectionName, String fieldName, String fieldValue, Class<T> className) {
		   Query query = new Query();
		   query.addCriteria(Criteria.where(fieldName).is(fieldValue));    
		  return mongoTemplate.count(query, className, collectionName);
		 }	
	
	public UserBean getUserDetails(String userName) throws Exception {
		UserBean userBean = getObjectByField(BgipConstants.USER_COLLECTION, "userName", userName, UserBean.class);
		return userBean;
	}

	public <T> List<T> getAllObjects(String collectionName, Class<T> className) throws Exception {
		List<T> objectList = null;
		objectList = mongoTemplate.findAll(className, collectionName);
		return objectList;
	}
	
	public <T> T getObject(String collectionName, Query query, Class<T> className) throws Exception {
		T obj = null;
		obj = mongoTemplate.findOne(query, className, collectionName);
		return obj;
	}
	
	public <T> List<T> getObjects(String collectionName, Query query, Class<T> className) throws Exception {
		List<T> objectList = null;
		objectList = mongoTemplate.find(query, className, collectionName);
		return objectList;
	}
	
	public <T> List<T> getObjectsByField(String collectionName, String fieldName, String fieldValue, Class<T> className) throws Exception {
		List<T> objectList = null;
		Query query = new Query(Criteria.where(fieldName).is(fieldValue));
		objectList = mongoTemplate.find(query, className, collectionName);
		return objectList;
	}

	public <T> T getObjectByField(String collectionName, String fieldName, String fieldValue, Class<T> className) throws Exception {
		T object = null;
		Query query = new Query(Criteria.where(fieldName).is(fieldValue));
		object = mongoTemplate.findOne(query, className, collectionName);
		return object;
	}
	
	
	public <T> WriteResult updateByField(String collectionName,
			String fieldName, String loginUser, String key, boolean b)
			throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName).is(loginUser));
		Update update = new Update();
		update.set(key, b);
		// object = mongoTemplate.updateFirst(query, Update.update(key,
		// keyvalue), className);
		object = mongoTemplate.updateFirst(query, update,
				collectionName);
		return object;
	}
	
	public <T> WriteResult updateByField(String collectionName,
			String fieldName, ObjectId objectId, String key, boolean b)
			throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName).is(objectId));
		Update update = new Update();
		update.set(key, b);
		// object = mongoTemplate.updateFirst(query, Update.update(key,
		// keyvalue), className);
		object = mongoTemplate.updateFirst(query, update,
				collectionName);
		return object;
	}
	
	public <T> WriteResult updateByField(String collectionName,
			String fieldName, String objectId, String key, String b)
			throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName).is(objectId));
		Update update = new Update();
		update.set(key, b);
		// object = mongoTemplate.updateFirst(query, Update.update(key,
		// keyvalue), className);
		object = mongoTemplate.updateFirst(query, update,
				collectionName);
		return object;
	}
	
	
	
	public <T> WriteResult updateBy2Fields(String collectionName,
			String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, String key, String keyvalue)
			throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
		Update update = new Update();
		update.set(key, keyvalue);
		// object = mongoTemplate.updateFirst(query, Update.update(key,
		// keyvalue), className);
		object = mongoTemplate.updateMulti(query, update,
				collectionName);
		return object;
	}
	
	
	
	public <T> WriteResult updateBy2Fields(String collectionName,
			String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, String key, boolean keyvalue)
			throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
		Update update = new Update();
		update.set(key, keyvalue);
		// object = mongoTemplate.updateFirst(query, Update.update(key,
		// keyvalue), className);
		object = mongoTemplate.updateFirst(query, update,
				collectionName);
		return object;
	}
	
	public <T> WriteResult updateMulti(String collectionName, String fieldName, String fieldValue,String key1, String keyvalue1,String key2, String keyvalue2) throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName).is(fieldValue));
		Update update = new Update();
		update.set(key1, keyvalue1);
		update.set(key2, keyvalue2);
		object = mongoTemplate.updateMulti(query, update, collectionName);
		return object;
	}
	
	
	
	public <T> T getObjectByID(String collectionName, String objectID, Class<T> className) throws Exception {
		T object = null;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(objectID));
		object = mongoTemplate.findOne(query, className, collectionName);
		return object;
	}
	
	public <T> WriteResult updateByObjectId(String collectionName,
			String fieldName, ObjectId objectId, String key, boolean b)
			throws Exception {
		WriteResult object = null;
		Query query = new Query(Criteria.where(fieldName).is(objectId));
		Update update = new Update();
		update.set(key, b);
		object = mongoTemplate.updateFirst(query, update,
				collectionName);
		return object;
	}
	

	
	public <T> List<T> getObjectsByID(String collectionName, List<String> objectIDList, Class<T> className) throws Exception {
		List<T> objectList = new ArrayList<T>();
		Criteria[] orCriteriaArray = new Criteria[objectIDList.size()];
		for (String idString : objectIDList) {
			orCriteriaArray[0] = Criteria.where("id").is(idString);
		}
		Criteria orCriteria = new Criteria();
		orCriteria.orOperator(orCriteriaArray);
		
		Query query = new Query();
		query.addCriteria(orCriteria);
		
		objectList = mongoTemplate.find(query, className, collectionName);

		
		return objectList;
	}
	
	public void deleteByField(String collectionName, String fieldName, String fieldValue) throws Exception {
		
		try {
			Query query = new Query(Criteria.where(fieldName).is(fieldValue));
			mongoTemplate.remove(query, collectionName);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}
	
	public <T> T  getObjectBy2Field(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2,Class<T> className) throws Exception {
		T object = null;
		try {
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
			mongoTemplate.findOne(query, className, collectionName);
				} catch (Exception e) {
					throw new Exception(e);
			}
		return object;
		}
		

	public void deleteBy2Field(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2) throws Exception {
		try {
			//Query query = new Query(new Criteria().andOperator(Criteria.where(fieldName1).is(fieldValue1),Criteria.where(fieldName2).is(fieldValue2)));
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
			mongoTemplate.remove(query, collectionName);
				} catch (Exception e) {
					throw new Exception(e);
			}
		}
	
	
	public void deleteBy3Field(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, String fieldName3, String fieldValue3) throws Exception {
		try {
			//Query query = new Query(new Criteria().andOperator(Criteria.where(fieldName1).is(fieldValue1),Criteria.where(fieldName2).is(fieldValue2)));
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2) .and(fieldName3).is(fieldValue3));
			mongoTemplate.remove(query, collectionName);
				} catch (Exception e) {
					throw new Exception(e);
			}
		}
		
	public <T> void deleteCollection(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, Class<T> className) throws Exception {
		try {
			//Query query = new Query(new Criteria().andOperator(Criteria.where(fieldName1).is(fieldValue1),Criteria.where(fieldName2).is(fieldValue2)));
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
			mongoTemplate.remove(query, className, collectionName);
				} catch (Exception e) {
					throw new Exception(e);
			}
		}
		
	public Object insertCollection(String collectionName, Object object) throws Exception {
		try {
			mongoTemplate.insert(object, collectionName);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		return object;
	}

	
	
	public Object insert(String collectionName, Object object) throws Exception {
		try {
			mongoTemplate.save(object, collectionName);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		return object;
	}

	public Object update(String collectionName, Object object) throws Exception {
		try {
			mongoTemplate.save(object, collectionName);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return object;
	}

	public void delete(String collectionName, Object object) throws Exception {
		
		try {
			mongoTemplate.remove(object, collectionName);
			
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}
	
	public void drop(String collectionName) throws Exception {
		
		try {
			mongoTemplate.dropCollection(collectionName);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}
	
	
	
	public static String getNextSeqId(DB db, String seqname) {
		String sequence_field = "seq"; // the name of the field which holds the sequence
		DBCollection seq = db.getCollection(seqname); // get the collection (this will create it if needed)
		// this object represents your "query", its analogous to a WHERE clause in SQL
		DBObject query = new BasicDBObject();
		query.put("_id", seqname); // where _id = the input sequence name
		// this object represents the "update" or the SET blah=blah in SQL
		DBObject change = new BasicDBObject(sequence_field, 1);
		DBObject update = new BasicDBObject("$inc", change); // the $inc here is a mongodb command for increment

		// Atomically updates the sequence field and returns the value for you
		DBObject res = seq.findAndModify(query, new BasicDBObject(), new BasicDBObject(), false, update, true, true);
		return res.get(sequence_field).toString();
	}
	
	
	
	public  <T> T findBy3Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2,String fieldName3, String fieldValue3, Class<T> className) throws Exception {
		T object = null;
		try {
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2) .and(fieldName3).is(fieldValue3));
			object = mongoTemplate.findOne(query, className, collectionName);
			return object;
				} catch (Exception e) {
					throw new Exception(e);
			}
		}	
	
	public  <T> T findBy3Fields(String collectionName, String fieldName1, ObjectId objectId, String fieldName2, String fieldValue2,String fieldName3, boolean b, Class<T> class1) throws Exception {
		T object = null;
		try {
			Query query = new Query(Criteria.where(fieldName1).is(objectId) .and(fieldName2).is(fieldValue2) .and(fieldName3).is(b));
			object = mongoTemplate.findOne(query, class1, collectionName);
			return object;
				} catch (Exception e) {
					throw new Exception(e);
			}
		}	
	
	
	public  <T> T findBy3Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2,String fieldName3, boolean fieldValue3, Class<T> className) throws Exception {
		T object = null;
		try {
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2) .and(fieldName3).is(fieldValue3));
			object = mongoTemplate.findOne(query, className, collectionName);
			return object;
				} catch (Exception e) {
					throw new Exception(e);
			}
		}	
	
	
	
	public  <T> T findBy4Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2,String fieldName3, String fieldValue3,String fieldName4, String fieldValue4, Class<T> className) throws Exception {
		T object = null;
		try {
			Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2) .and(fieldName3).is(fieldValue3) .and(fieldName4).is(fieldValue4));
			object = mongoTemplate.findOne(query, className, collectionName);
			return object;
				} catch (Exception e) {
					throw new Exception(e);
			}
		}


	public <T> List<T> getObjectsBy2Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, Class<T> className) throws Exception {
		List<T> objectList = null;
		Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
		objectList = mongoTemplate.find(query, className, collectionName);
		return objectList;
	}
	
	
	
	public <T> List<T> getObjectsBy2Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, boolean b, Class<T> class1) throws Exception {
		List<T> objectList = null;
		Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(b));
		objectList = mongoTemplate.find(query, class1, collectionName);
		return objectList;
	}
	
	
	
	
	
	public <T> List<T> getObjectsBy3Fields(String collectionName, String fieldName1, String fieldValue1, String fieldName2, String fieldValue2,String fieldName3, String fieldValue3, Class<T> className) throws Exception {
		List<T> objectList = null;
		Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2) . and(fieldName3).is(fieldValue3));
		objectList = mongoTemplate.find(query, className, collectionName);
		return objectList;
	}
	
	/*
	
	public <T> List<T> getObjectsByOrderAndPage(String collectionName,String fieldName1, String fieldValue1, String fieldName2, String fieldValue2,String fieldName3, Boolean fieldValue3, String sortField, Direction orderDir,Integer pageNumber, Integer pageSize, String distinctField, Class<T> className) throws Exception{
		
		//new Criteria();
//		Criteria criteria1 = Criteria.where(fieldName1).is(fieldValue1);
//		Criteria criteria2 = Criteria.where(fieldName2).is(fieldValue2);
//		Criteria criteria3 = (new Criteria().orOperator(criteria1,criteria2));		
		Query query = new Query(Criteria.where(fieldName2).is(fieldValue2).and(fieldName3).is(fieldValue3)).with(new Sort(new Order(orderDir, sortField))).skip((pageSize)*(pageNumber-1)).limit(pageSize);
		List<T> objectList = mongoTemplate.find(query, className, collectionName);
		//Query query1 =  new Query(Criteria.where(fieldName1).is(fieldValue1)).with(new Sort(new Order(orderDir, sortField))).skip((pageSize)*(pageNumber-1)).limit(pageSize);
		//List<String> postMsgList = mongoTemplate.getCollection(collectionName).distinct(distinctField, query.getQueryObject());
				//objectList =		getMongoDB().getCollection(collectionName).distinct(distinctField, query.getQueryObject());
		
//		Aggregation agg = newAggregation(
//				match(Criteria.where(fieldName1).is(fieldValue1)),			
//				
//				sort(Sort.Direction.DESC, sortField),
//				limit(pageSize),
//				skip(pageNumber-1)
//					
//			);
		 
		// Query query = new Query(Criteria.where(fieldName1).is(fieldValue1) .and(fieldName2).is(fieldValue2));
		
		return objectList;
		
	}
	public DB getMongoDB() throws Exception {
		return mongo.getDB(DB_NAME);
	}
}

		*/
	
public <T> List<T> getObjectsByOrderAndPage(String collectionName,String fieldName1, String fieldValue1, 
		String fieldName2, String fieldValue2,String fieldName3, String fieldValue3, String sortField, 
		Direction orderDir,Integer pageNumber, Integer pageSize, String distinctField, Class<T> className) throws Exception{
		

	//boolean shareStatus = true;
	//Criteria criteria1 = Criteria.where(fieldName1).is(fieldValue1);
	Criteria criteria1 = Criteria.where(fieldName2).is(fieldValue2);
	Criteria criteria2 = Criteria.where("hide").is(false).and(fieldName2).is(fieldValue2);
	//Criteria criteria3 = (new Criteria().orOperator(criteria1,criteria2));
	
	Query query = new Query(criteria2.and(fieldName3).is(fieldValue3)).with(new Sort(new Order(orderDir, sortField))).skip((pageSize)*(pageNumber-1)).limit(pageSize);
	List<T> objectList = mongoTemplate.find(query, className, collectionName);
	return objectList;
}

public DB getMongoDB() throws Exception {
	return mongo.getDB(DB_NAME);
}
	
	
	
	
	public <T> List<T> getObjectsByOrderAndPage(String collectionName,String fieldName1, String fieldValue1, 
			String sortField, 
			Direction orderDir,Integer pageNumber, Integer pageSize, String distinctField, Class<T> className) throws Exception{
			
		Criteria criteria1 = Criteria.where(fieldName1).is(fieldValue1);
		//Criteria criteria1 = Criteria.where("hide").is(false).and(fieldName1).is(fieldValue1);
		
		Query query = new Query(criteria1.and("hide").is(false)).with(new Sort(new Order(orderDir, sortField))).skip((pageSize)*(pageNumber-1)).limit(pageSize);

		List<T> objectList = mongoTemplate.find(query, className, collectionName);
		
		return objectList;
		
	}
	
	
	public <T> List<T> getObjectsByOrderAndPage(String collectionName,String fieldName1, String fieldValue1, String fieldName2, String fieldValue2, 
			String sortField, 
			Direction orderDir,Integer pageNumber, Integer pageSize, Class<T> className) throws Exception{
			
		Criteria criteria1 = Criteria.where(fieldName1).is(fieldValue1);
		
		Query query = new Query(criteria1).with(new Sort(new Order(orderDir, sortField))).skip((pageSize)*(pageNumber-1)).limit(pageSize);

		List<T> objectList = mongoTemplate.find(query, className, collectionName);
		
		return objectList;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}