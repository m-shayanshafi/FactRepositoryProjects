package net.divinerpg.client.render.entity.twilight.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSorcerer extends ModelBase {
    ModelRenderer body;
    ModelRenderer rightarm;
    ModelRenderer staffProngRight2;
    ModelRenderer rightleg;
    ModelRenderer leftleg;
    ModelRenderer leftarm;
    ModelRenderer staffPoleRight;
    ModelRenderer staffCapRight;
    ModelRenderer staffProngRight3;
    ModelRenderer staffProngRight1;
    ModelRenderer staffProngRight4;
    ModelRenderer head;
    ModelRenderer ray2;
    ModelRenderer ray8;
    ModelRenderer ray6;
    ModelRenderer ray4;
    ModelRenderer ray1;
    ModelRenderer ray7;
    ModelRenderer ray5;
    ModelRenderer ray3;
    ModelRenderer staffPoleLeft;
    ModelRenderer staffCapLeft;
    ModelRenderer staffProngLeft1;
    ModelRenderer staffProngLeft2;
    ModelRenderer staffProngLeft4;
    ModelRenderer staffProngLeft3;

    public ModelSorcerer() {
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this, 24, 0);
        body.addBox(-4F, 0F, -2F, 8, 12, 4);
        body.setRotationPoint(0F, 0F, 0F);
        body.setTextureSize(64, 32);
        body.mirror = true;
        setRotation(body, 0F, 0F, 0F);
        rightarm = new ModelRenderer(this, 10, 23);
        rightarm.addBox(-6F, 4F, -2F, 4, 4, 4);
        rightarm.setRotationPoint(-4F, 2F, -2F);
        rightarm.setTextureSize(64, 32);
        rightarm.mirror = true;
        setRotation(rightarm, 0F, 0F, 0F);
        staffProngRight2 = new ModelRenderer(this, 40, 20);
        staffProngRight2.addBox(-3F, -7F, -3F, 1, 2, 1);
        staffProngRight2.setRotationPoint(-4F, 2F, 0F);
        staffProngRight2.setTextureSize(64, 32);
        staffProngRight2.mirror = true;
        setRotation(staffProngRight2, 0F, 0F, 0F);
        rightleg = new ModelRenderer(this, 0, 12);
        rightleg.addBox(-2F, 10F, -6F, 4, 2, 8);
        rightleg.setRotationPoint(-3F, 12F, 0F);
        rightleg.setTextureSize(64, 32);
        rightleg.mirror = true;
        setRotation(rightleg, 0F, 0F, 0F);
        leftleg = new ModelRenderer(this, 0, 12);
        leftleg.addBox(-2F, 10F, -6F, 4, 2, 8);
        leftleg.setRotationPoint(3F, 12F, 0F);
        leftleg.setTextureSize(64, 32);
        leftleg.mirror = true;
        setRotation(leftleg, 0F, 0F, 0F);
        leftarm = new ModelRenderer(this, 10, 23);
        leftarm.addBox(1F, 4F, -4F, 4, 4, 4);
        leftarm.setRotationPoint(5F, 2F, 0F);
        leftarm.setTextureSize(64, 32);
        leftarm.mirror = true;
        setRotation(leftarm, 0F, 0F, 0F);
        staffPoleRight = new ModelRenderer(this, 53, 0);
        staffPoleRight.addBox(-5F, -4F, -5F, 2, 22, 2);
        staffPoleRight.setRotationPoint(-4F, 2F, 0F);
        staffPoleRight.setTextureSize(64, 32);
        staffPoleRight.mirror = true;
        setRotation(staffPoleRight, 0F, 0F, 0F);
        staffCapRight = new ModelRenderer(this, 40, 25);
        staffCapRight.addBox(-6F, -5F, -6F, 4, 2, 4);
        staffCapRight.setRotationPoint(-4F, 2F, 0F);
        staffCapRight.setTextureSize(64, 32);
        staffCapRight.mirror = true;
        setRotation(staffCapRight, 0F, 0F, 0F);
        staffProngRight3 = new ModelRenderer(this, 40, 20);
        staffProngRight3.addBox(-6F, -7F, -3F, 1, 2, 1);
        staffProngRight3.setRotationPoint(-4F, 2F, 0F);
        staffProngRight3.setTextureSize(64, 32);
        staffProngRight3.mirror = true;
        setRotation(staffProngRight3, 0F, 0F, 0F);
        staffProngRight1 = new ModelRenderer(this, 40, 20);
        staffProngRight1.addBox(-3F, -7F, -6F, 1, 2, 1);
        staffProngRight1.setRotationPoint(-4F, 2F, 0F);
        staffProngRight1.setTextureSize(64, 32);
        staffProngRight1.mirror = true;
        setRotation(staffProngRight1, 0F, 0F, 0F);
        staffProngRight4 = new ModelRenderer(this, 40, 20);
        staffProngRight4.addBox(-6F, -7F, -6F, 1, 2, 1);
        staffProngRight4.setRotationPoint(-4F, 2F, 0F);
        staffProngRight4.setTextureSize(64, 32);
        staffProngRight4.mirror = true;
        setRotation(staffProngRight4, 0F, 0F, 0F);
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-3F, -6F, -3F, 6, 6, 6);
        head.setRotationPoint(0F, -5F, -1F);
        head.setTextureSize(64, 32);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        ray2 = new ModelRenderer(this, 0, 23);
        ray2.addBox(-7F, -1F, -1F, 2, 2, 2);
        ray2.setRotationPoint(0F, -9F, -1F);
        ray2.setTextureSize(64, 32);
        ray2.mirror = true;
        setRotation(ray2, 0F, 0F, -0.7853982F);
        ray8 = new ModelRenderer(this, 0, 23);
        ray8.addBox(-1F, 6F, -1F, 2, 2, 2);
        ray8.setRotationPoint(0F, -9F, -1F);
        ray8.setTextureSize(64, 32);
        ray8.mirror = true;
        setRotation(ray8, 0F, 0F, -0.7853982F);
        ray6 = new ModelRenderer(this, 0, 23);
        ray6.addBox(5F, -1F, -1F, 2, 2, 2);
        ray6.setRotationPoint(0F, -9F, -1F);
        ray6.setTextureSize(64, 32);
        ray6.mirror = true;
        setRotation(ray6, 0F, 0F, -0.7853982F);
        ray4 = new ModelRenderer(this, 0, 23);
        ray4.addBox(-1F, -7F, -1F, 2, 2, 2);
        ray4.setRotationPoint(0F, -9F, -1F);
        ray4.setTextureSize(64, 32);
        ray4.mirror = true;
        setRotation(ray4, 0F, 0F, -0.7853982F);
        ray1 = new ModelRenderer(this, 0, 23);
        ray1.addBox(-1F, 6F, -1F, 2, 2, 2);
        ray1.setRotationPoint(0F, -9F, -1F);
        ray1.setTextureSize(64, 32);
        ray1.mirror = true;
        setRotation(ray1, 0F, 0F, 0F);
        ray7 = new ModelRenderer(this, 0, 23);
        ray7.addBox(5F, -1F, -1F, 2, 2, 2);
        ray7.setRotationPoint(0F, -9F, -1F);
        ray7.setTextureSize(64, 32);
        ray7.mirror = true;
        setRotation(ray7, 0F, 0F, 0F);
        ray5 = new ModelRenderer(this, 0, 23);
        ray5.addBox(-1F, -7F, -1F, 2, 2, 2);
        ray5.setRotationPoint(0F, -9F, -1F);
        ray5.setTextureSize(64, 32);
        ray5.mirror = true;
        setRotation(ray5, 0F, 0F, 0F);
        ray3 = new ModelRenderer(this, 0, 23);
        ray3.addBox(-7F, -1F, -1F, 2, 2, 2);
        ray3.setRotationPoint(0F, -9F, -1F);
        ray3.setTextureSize(64, 32);
        ray3.mirror = true;
        setRotation(ray3, 0F, 0F, 0F);
        staffPoleLeft = new ModelRenderer(this, 53, 0);
        staffPoleLeft.addBox(2F, -4F, -5F, 2, 22, 2);
        staffPoleLeft.setRotationPoint(5F, 2F, 0F);
        staffPoleLeft.setTextureSize(64, 32);
        staffPoleLeft.mirror = true;
        setRotation(staffPoleLeft, 0F, 0F, 0F);
        staffCapLeft = new ModelRenderer(this, 40, 25);
        staffCapLeft.addBox(1F, -5F, -6F, 4, 2, 4);
        staffCapLeft.setRotationPoint(5F, 2F, 0F);
        staffCapLeft.setTextureSize(64, 32);
        staffCapLeft.mirror = true;
        setRotation(staffCapLeft, 0F, 0F, 0F);
        staffProngLeft1 = new ModelRenderer(this, 40, 20);
        staffProngLeft1.addBox(4F, -7F, -6F, 1, 2, 1);
        staffProngLeft1.setRotationPoint(5F, 2F, 0F);
        staffProngLeft1.setTextureSize(64, 32);
        staffProngLeft1.mirror = true;
        setRotation(staffProngLeft1, 0F, 0F, 0F);
        staffProngLeft2 = new ModelRenderer(this, 40, 20);
        staffProngLeft2.addBox(4F, -7F, -3F, 1, 2, 1);
        staffProngLeft2.setRotationPoint(5F, 2F, 0F);
        staffProngLeft2.setTextureSize(64, 32);
        staffProngLeft2.mirror = true;
        setRotation(staffProngLeft2, 0F, 0F, 0F);
        staffProngLeft4 = new ModelRenderer(this, 40, 20);
        staffProngLeft4.addBox(1F, -7F, -6F, 1, 2, 1);
        staffProngLeft4.setRotationPoint(5F, 2F, 0F);
        staffProngLeft4.setTextureSize(64, 32);
        staffProngLeft4.mirror = true;
        setRotation(staffProngLeft4, 0F, 0F, 0F);
        staffProngLeft3 = new ModelRenderer(this, 40, 20);
        staffProngLeft3.addBox(1F, -7F, -3F, 1, 2, 1);
        staffProngLeft3.setRotationPoint(5F, 2F, 0F);
        staffProngLeft3.setTextureSize(64, 32);
        staffProngLeft3.mirror = true;
        setRotation(staffProngLeft3, 0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        rightarm.render(f5);
        staffProngRight2.render(f5);
        rightleg.render(f5);
        leftleg.render(f5);
        leftarm.render(f5);
        staffPoleRight.render(f5);
        staffCapRight.render(f5);
        staffProngRight3.render(f5);
        staffProngRight1.render(f5);
        staffProngRight4.render(f5);
        head.render(f5);
        ray2.render(f5);
        ray8.render(f5);
        ray6.render(f5);
        ray4.render(f5);
        ray1.render(f5);
        ray7.render(f5);
        ray5.render(f5);
        ray3.render(f5);
        staffPoleLeft.render(f5);
        staffCapLeft.render(f5);
        staffProngLeft1.render(f5);
        staffProngLeft2.render(f5);
        staffProngLeft4.render(f5);
        staffProngLeft3.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
        this.ray1.rotateAngleZ  = this.ray3.rotateAngleZ = this.ray5.rotateAngleZ = this.ray7.rotateAngleZ = f2/10;
        this.ray2.rotateAngleZ = this.ray4.rotateAngleZ = this.ray6.rotateAngleZ = this.ray8.rotateAngleZ = f2/10 - 0.7853982F;
        this.leftleg.rotateAngleX = this.rightarm.rotateAngleX = this.staffPoleRight.rotateAngleX = this.staffCapRight.rotateAngleX = this.staffProngRight1.rotateAngleX = this.staffProngRight2.rotateAngleX = this.staffProngRight3.rotateAngleX = this.staffProngRight4.rotateAngleX = MathHelper.cos(f)*f1*1.2f;
        this.rightleg.rotateAngleX = this.leftarm.rotateAngleX = this.staffPoleLeft.rotateAngleX = this.staffCapLeft.rotateAngleX = this.staffProngLeft1.rotateAngleX = this.staffProngLeft2.rotateAngleX = this.staffProngLeft3.rotateAngleX = this.staffProngLeft4.rotateAngleX = MathHelper.cos(f+(float)Math.PI)*f1*1.2f;
    }

}
