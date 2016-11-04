#version 410 core
//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;
uniform vec2 u_screenResolution;
uniform vec2 u_lightCoord[50];
uniform float u_lightIntensity[50];
uniform int u_actualLights;

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
   

   float brightest = 0;
   for(int i = 0;i < u_actualLights;i++){
   	float dis2Light = getDistance(gl_FragCoord.x, gl_FragCoord.y, u_lightCoord[i].x, u_lightCoord[i].y);
   	float dis2Light2 = (1-(min(dis2Light/(disFull * u_lightIntensity[i]), 1)));
   	brightest = brightest + dis2Light2 - brightest * dis2Light2;
   }
   texColor.rgb = texColor.rgb * (pow(brightest, 10));
   //final color
   gl_FragColor = texColor * vColor;
}
