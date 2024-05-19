package com.arlib.compose.test.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProblemResponse(
	@field:SerializedName("problems")
	val problems: List<ProblemsItem?>? = null
)

data class LabsItem(

	@field:SerializedName("missing_field")
	val missingField: String? = null
)

data class ProblemsItem(

	@field:SerializedName("Diabetes")
	val diabetes: List<DiseasesItem?>? = null,

	@field:SerializedName("Asthma")
	val asthma: List<AsthmaItem?>? = null
)

data class DiseasesItem(

	@field:SerializedName("labs")
	val labs: List<LabsItem?>? = null,

	@field:SerializedName("medications")
	val medications:List<MedicationsItem?>? = null
)

data class AsthmaItem(
	val any: Any? = null
)

data class AssociatedDrugItem(

	@field:SerializedName("dose")
	var dose: String? = null,

	@field:SerializedName("strength")
	var strength: String? = null,

	@field:SerializedName("name")
	var name: String? = null
) :Serializable {
	var problemName: String = ""
}

data class MedicationsClassesItem(

	@field:SerializedName("className2")
	val className2: List<ClassNameItem?>? = null,

	@field:SerializedName("className")
	val className: List<ClassNameItem?>? = null
)

data class ClassNameItem(

	@field:SerializedName("associatedDrug")
	val associatedDrug: List<AssociatedDrugItem?>? = null,

	@field:SerializedName("associatedDrug#2")
	val associatedDrug2: List<AssociatedDrugItem?>? = null,

):Serializable

data class MedicationsItem(

	@field:SerializedName("medicationsClasses")

	val medicationsClasses: List<MedicationsClassesItem?>? = null
)
