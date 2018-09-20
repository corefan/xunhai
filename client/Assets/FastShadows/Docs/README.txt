FAST SHADOWS

Fast Shadows is a fast, easy to use substitute for the Unity3D Blob Shadow Projector.
It is  heavily optimized for mobile devices. Raycasting determines where shadows should appear.
Shadows are a square mesh with a transparent material that hovers above a surface. 
Multiple shadows can share a single draw call.

Shadows are simple sprites, these are NOT volumetric shadows. Shadows may not appear properly on 
rough terrain and will not be clipped properly as the shadow passes over edges. However they are very,
very fast to render.

==============
QUICKSTART

Drag the FS_SimpleShadow script to an object in your scene. Press play and it will have a shadow.

It is recommended to do the following:
    - Set the layer mask so shadows don't appear in unwanted places.
    - If you want a shadow with a custom shape, duplicate the shadow material FS_ShadowMaterial, 
    	drag it to the "shadow material" field, replace the texture with a custom shape.
	- For best results set up your model with:
                            y - is up
                            z - is forward
                            x - is right	
	  If your default model state has non-zero rotations or non-uniform scaling then set up your hierarchy like this:
                         
                         GameObject (FS_SimpleShadow is attached to this. This is moved and rotated)
                               |- Model (with necessary rotations and non-uniform scaling)	
	
==============
DESCRIPTION OF OPTIONS

    LAYER MASK

      Shadows will only appear on layers selected here

    MAX PROJECTION DISTANCE

      This is the maximum distance that a shadow will be cast from an object. Shadows will
      fade as they approach this distance and then disappear

    SHADOW SIZE

      The size of the shadow.

    SHADOW HOVER HEIGHT

      The shadow will hover at this height above a surface

    STATIC

      If shadows will not move then setting a shadow to static can improve performance. Static shadows are
      calculated once at "Start" and are not recalculated again. A scene can have hundreds of static shadows
      with the only overhead being one draw call. No calculations take place in update and fixed update. Note that static 
      shadows are rendered with a separate draw call from non-static shadows. If you only have a few static shadows 
      you are probably better off not using the static option.

    SHADOW MATERIAL

      The material used by the shadow. Shadows with different materials are rendered in different draw calls.
      If you want a custom shadow shape then:
             - duplicate the FS_ShadowMaterial found in the Resources folder
             - drag the new material to the "shadow material" field
             - replace the texture in the new material with a custom one
      Multiple shadow shapes can share the same material by using a texture atlas and setting the UV coordinates.

    UV RECT

      Multiple shadow shapes can share the same material by using a texture atlas and setting the UV coordinates. 
      The rectangle specified here must be in a square of size 1 x 1.

    USE LIGHT SOURCE GAME OBJECT

      Incoming light direction can be described by:
        - A light direction vector (infinitely distant light source)
        - A game object (usually a light). Shadows will move and distort as an object moves relative to the light

    USE PERSPECTIVE PROJECTION

       This only works with a if "Use Light Source" has been selected. If checked then shadows will distort and grow as
       the shadow-casting object approaches the light source.
       
    CULL NON-VISIBLE
    
	   Cull shadows that are outside the fustrum of Camera.main. If you have multiple cameras then
	   shadows may not be culled correctly. There is a performance cost to the culling test so if most of
	   your shadows are always visible then you probably what this turned off.

================
TROUBLESHOOTING AND TIPS

     If shadows do not appear
		 - Check that you have a collider on the objects where you want the shadow to appear.

         - Shadows only appear in play mode. They don't not appear in the editor.

         - Check the layer mask. Shadows only appear on the selected layers. if the layer mask has been set to
           'Everything' the shadow may be appearing on the shadow casting object itself.

         - Check that the ShadowMaterial has been set up correctly.

         - Check the "Max projection distance" value. Shadows will not appear beyond this distance.
         
         - Turn on gizmos to see the frustum for the shadow
         
         - Check the origin of the shadowcasting object. Rays are cast from here. If the origin is at the base of the object
           and the edge of the collider is also at the base of the object then the object can sink into the floor slightly
           and rays are cast from under the floor. The solution is to attach the Fast Shadow script to a child
           GameObject that is translated up to the center of the shadowcasting object.

     Strange shadow rotation

          Fast Shadows tries to rotate the shadow to match the orientation of an object. It assumes:
                            y - is up
                            z - is forward
                            x - is right
          
          Strange effects can happen if your default model state has non-zero rotations or non-uniform scaling.
          If your model is not set up this way then fast shadows can still be used by setting up your hierarchy like this:
                          
                         GameObject (FS_SimpleShadow is attached to this. This is moved and rotated)
                               |- Model (with necessary rotations and non-uniform scaling)

     Debugging 

        Gizmos are drawn in the scene view when playing.
                     - Enable gizmos
                     - Click play
                     - Click pause and switch the scene view. A frustum should be visible for the shadow.





    

    



