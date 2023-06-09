package manager.vdc.financialmanagement.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import manager.vdc.financialmanagement.main.model.m_r.Category
import manager.vdc.financialmanagement.main.model.query_model.CategoryWithDetails

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM category ORDER BY idCategory DESC ")
    fun getAllCategoriesLive(): LiveData<List<Category>>

    @Query("SELECT * FROM category ORDER BY idCategory DESC ")
    fun getAllCategories(): List<Category>

    @Query("SELECT * FROM category WHERE idCategory=:categoryId")
    fun getCategoryById(categoryId: Int): LiveData<Category>

    @Query("SELECT * FROM category WHERE type = :type ORDER BY idCategory DESC")
    fun getCategoryByType(type: String): List<Category>

    @Query("SELECT * FROM category " +
            "INNER JOIN account ON category.idAccount = account.idAccount ")
    fun getAllCategoryDetails(): List<CategoryWithDetails>

    @Query("SELECT * FROM category " +
            "INNER JOIN account ON category.idAccount = account.idAccount " +
            "WHERE type = :type ORDER BY idCategory DESC")
    fun getAllCategoryDetailsByType(type: Int): List<CategoryWithDetails>

}
