package com.gamedev.sjm.glesmvpdemo.SimpleEngine.Util.MatrixUtil;

import android.opengl.Matrix;

import com.gamedev.sjm.glesmvpdemo.SimpleEngine.Util.MathUtil.Vector3;

/**
 * 这里要注意,OpenGL的规范:
 *      1. 所有向量作为列矩阵对待
 *      2. 矩阵的顺序是一列一列的:
 *  m[offset +  0] m[offset +  4] m[offset +  8] m[offset + 12]
 *  m[offset +  1] m[offset +  5] m[offset +  9] m[offset + 13]
 *  m[offset +  2] m[offset +  6] m[offset + 10] m[offset + 14]
 *  m[offset +  3] m[offset +  7] m[offset + 11] m[offset + 15]
 *
 */
public class MatrixUtil {

    private static final Vector3 upDir = new Vector3(0,1,0);
    private static final Vector3 rightDir = new Vector3(1,0,0);
    private static final Vector3 forwardDir = new Vector3(0,0,1);

    public static float[] GetModelMatrix(
            float posX,float posY,float posZ,
            float angleX,float angleY,float angleZ,
            float scaleX,float scaleY,float scaleZ
    ){
        float[] mMatrix = new float[4*4];
        // 初始化模型-世界变换矩阵为单位矩阵
        Matrix.setIdentityM(mMatrix,0);

        // 变换顺序为 缩放 - 旋转 - 平移
        // 注意这里向量是列矩阵,矩阵乘法要左乘

        Matrix.multiplyMM(mMatrix,0,
                GetRotationMatrix(angleX,angleY,angleZ),
                0,
                GetScaleMatrix(scaleX,scaleY,scaleZ),
                0
        );

        Matrix.multiplyMM(mMatrix,0,
                GetTranslateMatrix(posX,posY,posZ),
                0,
                mMatrix,
                0
        );

        return mMatrix;
    }

    /**
     * UVN摄像机模型的观察矩阵
     * @param pos 摄像机位置
     * @param targetPos 摄像机观察位置
     * @param up  摄像机指向上的位置
     * @return
     */
    public static float[] GetViewMatrix(
            Vector3 pos,
            Vector3 targetPos,
            Vector3 up
    ){
        float[] matrix = new float[4*4];
        Matrix.setIdentityM(matrix,0);
        Matrix.setLookAtM(matrix,0,
                pos.x,pos.y,pos.z,
                targetPos.x,targetPos.y,targetPos.z,
                up.x,up.y,up.z);
        return matrix;
    }

    /**
     * 欧拉模型的摄像机,给定相机位置和当前相机的旋转来决定投影矩阵
     * @param pos
     * @param rotation
     * @return
     */
    public static float[] GetViewMatrix(
            Vector3 pos,
            Vector3 rotation
    ){
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix,0);

        // 按照 缩放 - 旋转 - 位移的顺序进行变换
        float[] rMatrix = GetRotationMatrix(-rotation.x,-rotation.y,-rotation.z);
        float[] tMatrix = GetTranslateMatrix(-pos.x,-pos.y,-pos.z);
        Matrix.multiplyMM(matrix,0,tMatrix,0,rMatrix,0);
        return matrix;
    }

    /**
     * 获得透视投影的投影矩阵
     * @param near 摄像机距离近平面的距离
     * @param far  摄像机距离远平面的距离
     * @return
     */
    public static float[] GetProjectFrustumMatrix(
            float near,float far,
            float fovAngle,float aspect
    ){
        float[] matrix = new float[4*4];
        Matrix.setIdentityM(matrix,0);
        Matrix.perspectiveM(matrix,
                0,
                fovAngle,aspect,
                near,far);

        return matrix;
    }

    public static float[] GetScaleMatrix(
            float scaleX,float scaleY,float scaleZ
    ){
        float[] matrix = new float[4*4];
        Matrix.setIdentityM(matrix,0);
        Matrix.scaleM(matrix,0,scaleX,scaleY,scaleZ);
        return matrix;
    }


    public static float[] GetRotationMatrix(
            float angleX, float angleY,float angleZ
    ){
//        float[] xMatrix = new float[4*4];
//        float[] yMatrix = new float[4*4];
//        float[] zMatrix = new float[4*4];
//
//        // 绕x轴旋转angleX度
//        Matrix.setRotateM(xMatrix,0,angleX,rightDir.x,rightDir.y,rightDir.z);
//        // 绕z轴旋转angleZ度
//        Matrix.setRotateM(zMatrix,0,angleZ,forwardDir.x,forwardDir.y,forwardDir.z);
//        // 绕y轴旋转angleY度
//        Matrix.setRotateM(yMatrix,0,angleY,upDir.x,upDir.y,upDir.z);
//
//        // 旋转矩阵,旋转顺序是zxy(旋转后轴不变的情况),这里以轴变换的形式,
//        // 则旋转顺序为yxz,注意GLES里向量为列矩阵,即矩阵运算是左乘的
        float[] rotationMatrix = new float[4*4];
        Matrix.setRotateEulerM(rotationMatrix,0,angleX,angleY,angleZ);
//        Matrix.setIdentityM(rotationMatrix,0);
//        Matrix.multiplyMM(rotationMatrix,0,xMatrix,0,yMatrix,0);
//        Matrix.multiplyMM(rotationMatrix,0,zMatrix,0,rotationMatrix,0);

        return rotationMatrix;
    }

    public static float[] GetTranslateMatrix(
            float x,float y,float z
    ){
        float[] matrix = new float[4*4];
        Matrix.setIdentityM(matrix,0);
        Matrix.translateM(matrix,0,x,y,z);

        return matrix;
    }
}
