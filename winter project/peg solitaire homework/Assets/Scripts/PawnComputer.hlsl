#if defined(UNITY_PROCEDURAL_INSTANCING_ENABLED)
	StructuredBuffer<uint> _Hashes; // Hashes of whether pawn is to be rendered or not
#endif

float4 _Config;		// Configuration
float _StepSize;	// Distance between pawns
float _PawnSize;	// Size of the pawns
int _Selected;		// Index of selected pawn

void ConfigureProcedural () {
	#if defined(UNITY_PROCEDURAL_INSTANCING_ENABLED)
		
		// Board position of instance
		float v = floor(unity_InstanceID * _Config.w + 0.00001f);
        float u = unity_InstanceID - v * _Config.x;

        v -= _Config.z;
        u -= _Config.y;
		
		float height = 0.0;

		// If instance is the selected one render a little higher
		if(unity_InstanceID == _Selected){
			height = 0.25;
		}

		// Set world position
		unity_ObjectToWorld = 0.0;
		unity_ObjectToWorld._m03_m13_m23_m33 = float4(
			u * _StepSize * 0.5,
			height,
			v * _StepSize * 0.5,
			1.0
		);

		// Set size
		unity_ObjectToWorld._m00_m11_m22 = _Hashes[unity_InstanceID] * _PawnSize;
	#endif
}

// Obligatory functions for this file to run
void ShaderGraphFunction_float (float3 In, out float3 Out) {
	Out = In;
}

void ShaderGraphFunction_half (half3 In, out half3 Out) {
	Out = In;
}