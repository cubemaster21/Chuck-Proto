#version 410 core
#define M_PI 3.1415926535897932384626433832795
#define M_LIGHTLIM 50
//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform vec2 u_screenResolution;
uniform vec2 u_lightCoord[M_LIGHTLIM];
uniform float u_lightIntensity[M_LIGHTLIM];
uniform vec3 u_lightColor[M_LIGHTLIM];
uniform int u_actualLights;
uniform float u_ambientLight;

//"in" varyings from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

float getDistance(float x1, float y1, float x2, float y2){
	float dx = x2 - x1;
	float dy = y2 - y1;
	float dis = sqrt(dx * dx + dy * dy);
	return dis;
}
void main() {



   //sample the texture
   vec4 texColor = texture2D(u_texture, vTexCoord);
 
   float dis = getDistance(u_screenResolution.x / 2.0, u_screenResolution.y / 2.0, gl_FragCoord.x, gl_FragCoord.y);
   float disFull = getDistance(u_screenResolution.x / 2.0, u_screenResolution.y / 2.0, 0, 0);
   float disSq = (1-(dis / disFull));
   
   
   //Distance is measured in pixels
   
	vec3 finalTint = vec3(1,1,1);
   float brightest = u_ambientLight;
   for(int i = 0;i < u_actualLights;i++){
   	float dis2Light = getDistance(gl_FragCoord.x, gl_FragCoord.y, u_lightCoord[i].x, u_lightCoord[i].y);
   	float dis2Light2 = (1-(min(dis2Light/(disFull * u_lightIntensity[i]), 1)));
   	brightest = brightest + dis2Light2 - brightest * dis2Light2;
	finalTint.rgb = finalTint.rgb + (u_lightColor[i].rgb - finalTint.rgb) * dis2Light2;
   }
   
   
   
   
   
   //Apply Light Levels
   texColor.rgb = texColor.rgb * (sin(pow(brightest, 10) * M_PI / 2));

   //apply torchlight tint
   
   texColor.rgb = texColor.rgb * finalTint.rgb;
   
   //final color
   gl_FragColor = texColor * vColor;
}
