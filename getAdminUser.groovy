import com.liferay.portal.kernel.service.UserLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil
import com.liferay.portal.kernel.model.role.RoleConstants
import com.liferay.portal.kernel.model.User
import com.liferay.portal.kernel.model.Role
import java.util.Arrays

long administratorRoleId = 20103L
long[] users = UserLocalServiceUtil.getRoleUserIds(administratorRoleId)
workflowContext.put("adminUserId", users[0])


