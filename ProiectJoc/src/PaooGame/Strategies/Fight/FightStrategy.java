package PaooGame.Strategies.Fight;

import PaooGame.Entities.Enemy;
import PaooGame.Entities.Entity;
import PaooGame.States.State;

public class FightStrategy {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected int healthBarX;
    protected int healthBarY;
    protected int healthBarWidth;
    protected int healthBarHeight;
    protected String backgroundImgPath;
    protected Enemy enemy;
    protected float defence;
    protected State ownerState;

    public FightStrategy(FightStrategyBuilder builder){
        this.enemy = builder.enemy;
        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.width;
        this.height = builder.height;
        this.healthBarX = builder.healthBarX;
        this.healthBarY = builder.healthBarY;
        this.healthBarWidth = builder.healthBarWidth;
        this.healthBarHeight = builder.healthBarHeight;
        this.backgroundImgPath = builder.backgroundImgPath;
        this.defence = builder.defence;
        this.ownerState = builder.ownerState;
    }

    public void update(){
        this.enemy.setX(this.x);
        this.enemy.setY(this.y);
        this.enemy.setHealthBarX(this.healthBarX);
        this.enemy.setHealthBarY(this.healthBarY);
        this.enemy.setHealthBarWidth(this.healthBarWidth);
        this.enemy.setHealthBarHeight(this.healthBarHeight);
    }

    public void takeDamage(float damage){
        this.enemy.reduceHealth((1f-this.defence)*damage);
    }
    public float calculateDamage(float damage){
        return (1f-this.defence)*damage;
    }


    public static class FightStrategyBuilder {
        private float x = 0f;
        private float y = 0f;
        private float width = 0f;
        private float height = 0f;
        private int healthBarX = 0;
        private int healthBarY = 0;
        private int healthBarWidth = 0;
        private int healthBarHeight = 0;
        private String backgroundImgPath;
        private Enemy enemy = null;
        private float defence = 0f;
        private State ownerState = null;

        public FightStrategyBuilder(Enemy enemy){
            this.enemy = enemy;
        }
        public FightStrategyBuilder x(float x){
            this.x = x;
            return this;
        }
        public FightStrategyBuilder y(float y){
            this.y = y;
            return this;
        }
        public FightStrategyBuilder width(float width){
            this.width = width;
            return this;
        }
        public FightStrategyBuilder height(float height){
            this.height = height;
            return this;
        }
        public FightStrategyBuilder healthBarX(int healthBarX){
            this.healthBarX = healthBarX;
            return this;
        }
        public FightStrategyBuilder healthBarY(int healthBarY){
            this.healthBarY = healthBarY;
            return this;
        }
        public FightStrategyBuilder healthBarWidth(int healthBarWidth){
            this.healthBarWidth = healthBarWidth;
            return this;
        }
        public FightStrategyBuilder healthBarHeight(int healthBarHeight){
            this.healthBarHeight = healthBarHeight;
            return this;
        }
        public FightStrategyBuilder backgroundImgPath(String backgroundImgPath){
            this.backgroundImgPath = backgroundImgPath;
            return this;
        }
        public FightStrategyBuilder defence(float defence){
            this.defence = defence;
            return this;
        }
        public FightStrategyBuilder ownerState(State ownerState){
            this.ownerState = ownerState;
            return this;
        }

        public FightStrategy build(){
            return new FightStrategy(this);
        }
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public void setDefence(float defence) {
        this.defence = defence;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setHealthBarX(int healthBarX) {
        this.healthBarX = healthBarX;
    }

    public void setHealthBarY(int healthBarY) {
        this.healthBarY = healthBarY;
    }

    public void setHealthBarWidth(int healthBarWidth) {
        this.healthBarWidth = healthBarWidth;
    }

    public void setHealthBarHeight(int healthBarHeight) {
        this.healthBarHeight = healthBarHeight;
    }

    public void setBackgroundImgPath(String backgroundImgPath) {
        this.backgroundImgPath = backgroundImgPath;
    }



    public float getDefence() {
        return defence;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getHealthBarX() {
        return healthBarX;
    }

    public int getHealthBarY() {
        return healthBarY;
    }

    public int getHealthBarWidth() {
        return healthBarWidth;
    }

    public int getHealthBarHeight() {
        return healthBarHeight;
    }

    public String getBackgroundImgPath() {
        return this.backgroundImgPath;
    }

    public State getOwnerState(){
        return this.ownerState;
    }





}
