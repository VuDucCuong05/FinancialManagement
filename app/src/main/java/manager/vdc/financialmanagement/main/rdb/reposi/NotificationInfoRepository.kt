package manager.vdc.financialmanagement.main.rdb.reposi

import androidx.lifecycle.LiveData
import manager.vdc.financialmanagement.main.model.m_r.NotificationInfo
import manager.vdc.financialmanagement.main.rdb.inter.NotificationInfoDao

class NotificationInfoRepository(private val notificationInfoDao: NotificationInfoDao) {
    val allNotificationInfoLive: LiveData<List<NotificationInfo>> =
        notificationInfoDao.getAllNotificationInfoLive()
    val allNotificationInfo: List<NotificationInfo> = notificationInfoDao.getAllNotificationInfo()

    suspend fun insert(notificationInfo: NotificationInfo) {
        notificationInfoDao.insert(notificationInfo)
    }

    suspend fun delete(notificationInfo: NotificationInfo) {
        notificationInfoDao.delete(notificationInfo)
    }

    suspend fun update(notificationInfo: NotificationInfo) {
        notificationInfoDao.update(notificationInfo)
    }
}
