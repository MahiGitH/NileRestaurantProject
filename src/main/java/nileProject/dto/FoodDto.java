package nileProject.dto;

import nileProject.entity.Food;
import nileProject.form.FoodForm;
import nileProject.model.FoodInfo;
import nileProject.pagination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Date;

@Transactional
@Repository
public class FoodDto {

    @Autowired
    private SessionFactory sessionFactory;

    public Food findFood(String code) {
        try {
            String sql = "Select e from " + Food.class.getName() + " e Where e.code =:code ";

            Session session = this.sessionFactory.getCurrentSession();
            Query<Food> query = session.createQuery(sql, Food.class);
            query.setParameter("code", code);
            return (Food) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public FoodInfo findFoodInfo(String code) {
        Food food = this.findFood(code);
        if (food == null) {
            return null;
        }
        return new FoodInfo(food.getCode(), food.getName(), food.getPrice());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(FoodForm foodForm) {

        Session session = this.sessionFactory.getCurrentSession();
        String code = foodForm.getCode();

        Food food = null;

        boolean isNew = false;
        if (code != null) {
            food = this.findFood(code);
        }

        if (food == null) {
            isNew = true;
            food = new Food();
            food.setCreateDate(new Date());
        }
        food.setCode(code);
        food.setName(foodForm.getName());
        food.setPrice(foodForm.getPrice());

        if (foodForm.getFileData() != null) {
            byte[] image = null;
            try {
                image = foodForm.getFileData().getBytes();
            } catch (IOException e) {
            }
            if (image != null && image.length > 0) {
                food.setImage(image);
            }
        }

        if (isNew) {
            session.persist(food);
        }
        // If error in DB, Exceptions will be thrown out immediately
        session.flush();
    }

    public PaginationResult<FoodInfo> queryFoods(int page, int maxResult, int maxNavigationPage,
                                                    String likeName) {
        String sql = "Select new " + FoodInfo.class.getName() //
                + "(f.code, f.name, f.price) " + " from "//
                + Food.class.getName() + " f ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(f.name) like :likeName ";
        }
        sql += " order by f.createDate desc ";
        //
        Session session = this.sessionFactory.getCurrentSession();
        Query<FoodInfo> query = session.createQuery(sql, FoodInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<FoodInfo>(query, page, maxResult, maxNavigationPage);
    }

    public PaginationResult<FoodInfo> queryFoods(int page, int maxResult, int maxNavigationPage) {
        return queryFoods(page, maxResult, maxNavigationPage, null);
    }


}

