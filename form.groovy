import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecordVersion;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import java.util.Locale;

def normaliseValue(String value) {
    if (value == null || "".equals(value)) {
        return value;
    }
    return value.replaceAll("\\[\"","").replaceAll("\"\\]","");
}
final long recVerId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_ENTRY_CLASS_PK));
final DDMFormInstanceRecordVersion recVer = DDMFormInstanceRecordVersionLocalServiceUtil.getFormInstanceRecordVersion(recVerId);
final Locale locale = recVer.getDDMForm().getDefaultLocale();
final List<DDMFormFieldValue> formFieldVals = recVer.getDDMFormValues().getDDMFormFieldValues();
for (DDMFormFieldValue fmval : formFieldVals) {
    final String fieldReference = fmval.getFieldReference();
    final Value val = fmval.getValue();
    final String data = normaliseValue(val.getString(Locale.ROOT));
    workflowContext.put(fieldReference, data);
}
workflowContext.put("recVerId", recVerId);
workflowContext.put("locale", locale);

