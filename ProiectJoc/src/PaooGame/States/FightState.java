package PaooGame.States;

import PaooGame.Entities.Enemy;
import PaooGame.Animations.Animation;
import PaooGame.Animations.EffectsAnimations.EffectAnimation;
import PaooGame.Config.Constants;
import PaooGame.HUD.AttackButton;
import PaooGame.HUD.ImageButton;
import PaooGame.HUD.VerticalGradientBar;
import PaooGame.Input.MouseInput;
import PaooGame.RefLinks;
import PaooGame.Strategies.Fight.FightStrategy;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


/**
 * @class FightState
 * @brief Represents the turn-based combat state of the game.
 *
 * This state manages the interaction between the player (hero) and an enemy.
 * It handles turns, player actions (attack, flee, block), enemy actions,
 * animations, UI elements (health bars, buttons, popups), and transitions
 * to other states (DeathState, Victory leading back to PlayState).
 * It features a fade-in effect upon entering and fade-out effects for transitions.
 *
 * A blocking mechanic is implemented using a progress bar during the enemy's turn.
 * On the enemy turn the progress values will vary between 0 and 50.
 *
 */
public class FightState extends State {
    private Enemy enemy=null;                                       ///< The current enemy entity engaged in combat.
    private boolean isPlayerTurn;                                   ///< Flag indicating if it is currently the player's turn.
    private double blackIntensity = 1.0;                            ///< Intensity of the black overlay for the initial fade-in effect (0.0 to 1.0).
    private double fadeSpeed = 0.05;                                ///< Speed of the fade-in effect.

    private Timer timer;                                            ///< Timer used to introduce a delay before the enemy attacks.
    private Timer timer2;                                           ///< Timer used to introduce a delay before the player's turn starts after an enemy attack.
    private Timer timer3;                                           ///< Timer used to delay the transition to DeathState after hero's health reaches zero.
    private Timer popupTimer;                                       ///< Timer to control the duration of damage popups.
    private Timer waitForEnemyDeathTimer;                           ///< Timer to delay the transition to victory after the enemy's health reaches zero.
    private Timer fleeTimer;                                        ///< Timer to manage the cooldown of fleeing.

    private FightStrategy fightStrategy = null;                     ///< The current active fight strategy, determined by the enemy type.
    private FightStrategy tigerStrategy = null;                     ///< Specific fight strategy for the Tiger enemy.
    private FightStrategy wizardStrategy = null;                    ///< Specific fight strategy for the Wizard enemy.
    private FightStrategy minotaurStrategy = null;                  ///< Specific fight strategy for the Minotaur enemy.
    private FightStrategy basicSkeletonStrategy = null;             ///< Specific fight strategy for the Basic Skeleton enemy.
    private FightStrategy ghostStrategy = null;                     ///< Specific fight strategy for the Ghost enemy.
    private FightStrategy strongSkeletonStrategy = null;            ///< Specific fight strategy for the Strong Skeleton enemy.

    private int popupTimeInMillis = 700;                            ///< Duration in milliseconds for damage popups to be visible.
    private int timeoutInMillis = 1000;                             ///< Default timeout for general purpose timer (e.g., enemy attack delay).
    private int timeoutInMillisForDelayingPlayerTurn = 1500;        ///< Duration in milliseconds to wait before starting the player's turn after enemy attack.
    private int timeoutInMillisWaitForEnemyDeath = 1300;            ///< Duration in milliseconds to wait before transitioning to victory after enemy death.
    private int timeoutInMillisWaitForFlee = 3000;                  ///< Duration related to the flee action.
    private boolean isTimerStarted;                                 ///< Flag indicating if {@link #timer} has started.
    private boolean isTimerFinished;                                ///< Flag indicating if {@link #timer} has finished.
    private boolean isWaitingForPlayerTurn;                         ///< Flag indicating if the game is waiting for {@link #timer2} to finish before player's turn.
    private boolean didEnemyAttackAlready;                          ///< Flag indicating if the enemy has already performed its attack in the current turn.
    private boolean isTimer3Started = false;                        ///< Flag indicating if {@link #timer3} (hero death transition) has started.

    private boolean printingDamageReceivedPopup = false;            ///< Flag to control the display of the damage received popup.
    private boolean printingDamageDealtPopup = false;               ///< Flag to control the display of the damage dealt popup.
    private double latestDamageReceived = 0.0;                      ///< Stores the most recent damage value received by the player.
    private double latestDamageDealt = 0.0;                         ///< Stores the most recent damage value dealt to the enemy.

    private boolean transitioningToDeath = false;                   ///< Flag indicating if the state is currently transitioning to the DeathState.
    private boolean transitioningToVictory = false;                 ///< Flag indicating if the state is currently transitioning to a victory sequence.

    private double fadeToBlackProgress = 0.0;                       ///< Progress of the fade-to-black effect for transitions (0.0 to 1.0).
    private double fadeToBlackStep = 0.05;                          ///< Increment step for {@link #fadeToBlackProgress}.


    private int enemyTurnProgressTick = 0;                          ///< Tick counter for the enemy turn progress bar animation.
    private int enemyTurnProgressCap = 2;                           ///< Cap for {@link #enemyTurnProgressTick} before updating the progress bar value.
    private int enemyTurnProgress = 0;                              ///< Current value of the enemy turn progress bar (0-50), used for blocking.
    private VerticalGradientBar blockingBar;                        ///< UI element representing the blocking progress bar.
    private int progressOnSpace = 0;                                ///< The value of {@link #enemyTurnProgress} captured when the player presses space to block.

    private Animation attackAnimation;                              ///< Animation effect displayed when the player attacks.

    private AttackButton attackButton;                              ///< UI button for the player to initiate an attack.
    private ImageButton fleeButton;                                 ///< UI button for the player to attempt to flee combat.
    private boolean isFleeButtonPressed = false;                    ///< Flag indicating if the flee button has been pressed in the current turn.

    private int scrollX = 0;                                        ///< X-offset for the scrolling background.
    private int scrollSpeed = 1;                                    ///< Speed at which the background scrolls.

    /**
     * @brief Constructs a FightState object.
     *
     * Initializes timers, UI elements (buttons, progress bar), and flags for managing
     * the fight sequence. Sets up the player attack animation.
     * @param reflink A {@link RefLinks} object providing access to game-wide resources and utilities.
     */
    public FightState(RefLinks reflink){
        super(reflink);

        this.isTimerFinished = false;
        this.isTimerStarted = false;
        this.isWaitingForPlayerTurn = false;
        this.didEnemyAttackAlready = false;

        this.blockingBar = new VerticalGradientBar(50,50); // Max value 50, cap value 50
        this.blockingBar.setPosition(935,250);


        this.fleeButton = new ImageButton(this.reflink.getHero(),90,550,this.reflink.getTileCache().getSpecial(Constants.BOOT_ITEM_SHEET_PATH,Constants.BOOT_TILE_WIDTH,Constants.BOOT_TILE_HEIGHT,1));


        // Timer for enemy attack delay
        this.timer = new Timer(timeoutInMillis, e->{
            this.isTimerFinished = true;
            this.isTimerStarted = false;

        });
        // Timer for delay before player's turn after enemy attack
        this.timer2 = new Timer(timeoutInMillisForDelayingPlayerTurn,e->{
            this.isPlayerTurn = true;
            this.isWaitingForPlayerTurn = false;
            this.didEnemyAttackAlready = false;
            this.enemyTurnProgress = 0; // Reset progress bar
            this.blockingBar.updateValue(this.enemyTurnProgress);
            // Apply damage to hero, considering block
            this.reflink.getHero().reduceHealth(this.enemy.getDamage()* (  (100.0-this.progressOnSpace*1.5f)/100.0) *4 );
            this.latestDamageReceived = this.enemy.getDamage()* (  (100.0-this.progressOnSpace*1.5f)/100.0) *4;
            this.progressOnSpace = 0; // Reset block value
            this.printingDamageReceivedPopup = true;
            this.popupTimer.start(); // Show damage received popup

        });
        // Timer for delay before transitioning to DeathState
        this.timer3 = new Timer(1000,e->{
            this.transitioningToDeath = true;
        });

        // Timer for damage popup duration
        this.popupTimer = new Timer(this.popupTimeInMillis,e->{
            this.printingDamageReceivedPopup = false;
            this.latestDamageReceived = 0.0;
            this.printingDamageDealtPopup = false;
            this.latestDamageDealt = 0.0;
        });
        // Timer for delay before transitioning to victory
        this.waitForEnemyDeathTimer = new Timer(this.timeoutInMillisWaitForEnemyDeath,e->{
            transitioningToVictory = true;
        });
        // Timer related to flee action
        this.fleeTimer = new Timer(this.timeoutInMillisWaitForFlee,e->{
            this.reflink.getHero().setEngageReady(true); // Allow hero to engage again after fleeing
        });

        this.timer.setRepeats(false);
        this.timer2.setRepeats(false);
        this.timer3.setRepeats(false);
        this.fleeTimer.setRepeats(false);
        this.popupTimer.setRepeats(false);
        this.waitForEnemyDeathTimer.setRepeats(false);

        this.attackButton = new AttackButton(reflink.getHero(),310,620);

        this.isPlayerTurn = true; // Player starts first

        this.attackAnimation = new EffectAnimation(this.reflink,Constants.EFFECTS.ATTACK_EXPLOSION,8,5);
        this.attackAnimation.loadAnimation();
    }

    /**
     * @brief Updates the logic of the FightState.
     *
     * This method is called every game tick. It updates the fight strategy,
     * handles turn-based logic, checks for win/loss conditions (enemy or hero death),
     * manages transitions, and updates the scrolling background.
     */
    @Override
    public void update() {
        updateStrategy(); // Determine and update the correct fight strategy based on the enemy.
        this.attackAnimation.updateAnimation(); // Update player attack animation.
        handleTurnLogic(); // Manage player and enemy turns, input, and actions.

        // Check for enemy defeat
        if(this.enemy.getHealth() == 0 ){
            this.waitForEnemyDeathTimer.start(); // Start timer for victory transition.
        }
        // Check for player defeat
        if(this.reflink.getHero().getHealth()==0 && !isTimer3Started){
            this.isTimer3Started = true;
            this.timer3.start(); // Start timer for death transition.
        }

        handleTransitioningLogic(); // Manage fade-to-black transitions for victory/death.

        // Update background scrolling
        this.scrollX -= this.scrollSpeed;
        if(this.scrollX <= -Constants.WINDOW_WIDTH){
            this.scrollX = 0; // Loop background scroll.
        }
    }

    /**
     * @brief Draws the content of the FightState on the screen.
     *
     * This method is called every frame to render all visual elements of the combat scene,
     * including the background, enemy, UI elements (health bars, buttons, popups),
     * attack animations, and fade effects.
     * @param g The {@link Graphics} context to draw on.
     */
    @Override
    public void draw(Graphics g) {
        if(this.fightStrategy!=null){
            drawBG(g); // Draw scrolling background.

            Graphics2D g2d = (Graphics2D)g;
            this.blockingBar.draw(g2d); // Draw the blocking progress bar.
            handleInitialFadeFromBlack(g); // Apply initial fade-from-black effect.

            if(this.enemy!=null){
                this.enemy.draw(g); // Draw the enemy.
            }

            drawStrings(g); // Draw informational text (turn status, etc.).

            this.fightStrategy.getEnemy().DrawHealthBar(g); // Draw enemy health bar.
            reflink.getHero().DrawHealthBar(g); // Draw hero health bar.

            this.attackButton.draw(g2d); // Draw attack button.
            this.fleeButton.draw(g2d);   // Draw flee button.
            this.attackAnimation.paintAnimation(g,380,200,false,1); // Draw player attack animation.

            drawPopups(g); // Draw damage popups if active.
            drawTransitionFade(g); // Apply fade-to-black for transitions if active.
        }
    }

    /**
     * @brief Restores the FightState to its initial conditions.
     *
     * This method is called when transitioning into this state or when resetting it.
     * It resets all flags, timers, enemy references, strategies, UI states, and
     * effect variables to ensure a clean start for a new fight.
     */
    @Override
    public void restoreState() {
        blackIntensity = 1.0; // Reset for initial fade-in.
        fadeToBlackProgress = 0.0; // Reset for transition fade.
        if(this.enemy != null) { // Ensure enemy exists before accessing its state
            this.enemy.setCurrentState(Constants.ENEMY_STATES.FALLING); // Reset enemy animation state (if applicable).
        }

        this.transitioningToDeath = false;
        this.transitioningToVictory = false;
        this.isPlayerTurn = true;

        this.printingDamageDealtPopup = false;
        this.printingDamageReceivedPopup = false;
        this.isFleeButtonPressed = false;

        this.latestDamageDealt = 0.0;
        this.latestDamageReceived = 0.0;
        this.progressOnSpace = 0;
        this.enemyTurnProgress = 0;
        if(this.blockingBar!=null){
            this.blockingBar.updateValue(this.enemyTurnProgress); // Reset blocking bar.
        }
        this.enemyTurnProgressTick = 0;
        this.enemy = null; // Clear current enemy.
        // Clear all cached strategies.
        this.fightStrategy = null;
        this.tigerStrategy = null;
        this.basicSkeletonStrategy = null;
        this.strongSkeletonStrategy = null;
        this.minotaurStrategy = null;
        this.ghostStrategy = null;
        this.wizardStrategy = null;

        // Reset timer flags.
        this.isTimerStarted = false;
        this.isTimerFinished = false;
        this.isWaitingForPlayerTurn = false;
        this.didEnemyAttackAlready = false;
        this.isTimer3Started = false; // Ensure hero death timer flag is reset.


        // Stop all timers.
        if (timer != null) timer.stop();
        if (timer2 != null) timer2.stop();
        if (timer3 != null) timer3.stop();
        if (popupTimer != null) popupTimer.stop();
        if (waitForEnemyDeathTimer != null) waitForEnemyDeathTimer.stop();
    }

    /**
     * @brief Updates or initializes the fight strategy based on the current enemy.
     *
     * If a strategy for the current enemy type has not been created yet, this method
     * instantiates it using a builder pattern. It then sets the active {@link #fightStrategy}
     * to the appropriate one and updates it, along with the enemy and hero's health bar positions.
     */
    private void updateStrategy(){
        // Initialize strategies if they are null (first time encountering enemy type)
        if(this.tigerStrategy ==null){ // Check one strategy; if null, others likely are too.
            if(this.enemy!=null){
                switch (this.enemy.getName()){
                    case Constants.TIGER_NAME:
                        this.tigerStrategy = new FightStrategy.FightStrategyBuilder(this.enemy)
                                .x(480)
                                .y(100)
                                .width(this.enemy.getWidth())
                                .height(this.enemy.getHeight())
                                .healthBarX(460)
                                .healthBarY(80)
                                .healthBarWidth(this.enemy.getHealthBarWidth())
                                .healthBarHeight(this.enemy.getHealthBarHeight())
                                .backgroundImgPath(Constants.TIGER_FIGHT_BG_PATH)
                                .defence(Constants.TIGER_DEFENCE)
                                .ownerState(reflink.getGame().getLevel1State()).
                                build();
                        break;
                    case Constants.BASIC_SKELETON_NAME:
                        this.basicSkeletonStrategy = new FightStrategy.FightStrategyBuilder(this.enemy)
                                .x(285)
                                .y(0)
                                .width(this.enemy.getWidth())
                                .height(this.enemy.getHeight())
                                .healthBarX(460)
                                .healthBarY(80)
                                .healthBarWidth(this.enemy.getHealthBarWidth())
                                .healthBarHeight(this.enemy.getHealthBarHeight())
                                .backgroundImgPath(Constants.BASIC_SKELETON_FIGHT_BG_PATH)
                                .defence(Constants.BASIC_SKELETON_DEFENCE)
                                .ownerState(reflink.getGame().getLevel2State())
                                .build();
                        break;
                    case Constants.WIZARD_NAME:
                        this.wizardStrategy = new FightStrategy.FightStrategyBuilder(this.enemy)
                                .x(340)
                                .y(60)
                                .width(this.enemy.getWidth())
                                .height(this.enemy.getHeight())
                                .healthBarX(265)
                                .healthBarY(120)
                                .healthBarWidth(Constants.WIZARD_HEALTH_BAR_WIDTH)
                                .healthBarHeight(Constants.WIZARD_HEALTH_BAR_HEIGHT )
                                .backgroundImgPath(Constants.WIZARD_FIGHT_BG_PATH)
                                .defence(Constants.WIZARD_DEFENCE)
                                .ownerState(reflink.getGame().getLevel3State())
                                .build();
                        break;
                    case Constants.MINOTAUR_NAME:
                        this.minotaurStrategy = new FightStrategy.FightStrategyBuilder(this.enemy)
                                .x(20)
                                .y(0)
                                .width(this.enemy.getWidth())
                                .height(this.enemy.getHeight())
                                .healthBarX(410)
                                .healthBarY(80)
                                .healthBarWidth(Constants.MINOTAUR_HEALTH_BAR_WIDTH)
                                .healthBarHeight(Constants.MINOTAUR_HEALTH_BAR_HEIGHT)
                                .backgroundImgPath(Constants.MINOTAUR_FIGHT_BG_PATH)
                                .defence(Constants.MINOTAUR_DEFENCE)
                                .ownerState(reflink.getGame().getLevel3State())
                                .build();
                        break;
                    case Constants.GHOST_NAME:
                        this.ghostStrategy = new FightStrategy.FightStrategyBuilder(this.enemy)
                                .x(330)
                                .y(80)
                                .width(this.enemy.getWidth())
                                .height(this.enemy.getHeight())
                                .healthBarX(460)
                                .healthBarY(80)
                                .healthBarWidth(Constants.GHOST_HEALTH_BAR_WIDTH)
                                .healthBarHeight(Constants.GHOST_HEALTH_BAR_HEIGHT)
                                .backgroundImgPath(Constants.GHOST_FIGHT_BG_PATH)
                                .defence(Constants.GHOST_DEFENCE)
                                .ownerState(reflink.getGame().getLevel3State())
                                .build();
                        break;
                    case Constants.STRONG_SKELETON_NAME:
                        this.strongSkeletonStrategy = new FightStrategy.FightStrategyBuilder(this.enemy)
                                .x(260)
                                .y(30)
                                .width(this.enemy.getWidth())
                                .height(this.enemy.getHeight())
                                .healthBarX(405)
                                .healthBarY(80)
                                .healthBarWidth(Constants.STRONG_SKELETON_HEALTH_BAR_WIDTH)
                                .healthBarHeight(Constants.STRONG_SKELETON_HEALTH_BAR_HEIGHT)
                                .backgroundImgPath(Constants.STRONG_SKELETON_BG_PATH)
                                .defence(Constants.STRONG_SKELETON_DEFENCE)
                                .ownerState(reflink.getGame().getLevel2State())
                                .build();
                        break;
                }
            }
        }

        // Set the active fightStrategy based on the current enemy's name
        if(this.enemy!=null){
            switch (this.enemy.getName()){
                case Constants.TIGER_NAME:
                    this.fightStrategy = tigerStrategy;
                    break;
                case Constants.BASIC_SKELETON_NAME:
                    this.fightStrategy = basicSkeletonStrategy;
                    break;
                case Constants.WIZARD_NAME:
                    this.fightStrategy = wizardStrategy;
                    break;
                case Constants.MINOTAUR_NAME:
                    this.fightStrategy = minotaurStrategy;
                    break;
                case Constants.GHOST_NAME:
                    this.fightStrategy = ghostStrategy;
                    break;
                case Constants.STRONG_SKELETON_NAME:
                    this.fightStrategy = strongSkeletonStrategy;
                    break;
            }
            this.fightStrategy.update(); // Update the active strategy
            this.enemy.update(); // Update the enemy entity

            // Configure hero's health bar position for fight screen
            this.reflink.getHero().setHealthBarX(20);
            this.reflink.getHero().setHealthBarY(650);
            this.reflink.getHero().setHealthBarWidth(200);
            this.reflink.getHero().setHealthBarHeight(40);
        }
    }

    /**
     * @brief Handles the logic for transitioning to DeathState or PlayState (on victory).
     *
     * This method checks the {@link #transitioningToDeath} and {@link #transitioningToVictory} flags.
     * If a transition is active and the fade-to-black effect ({@link #fadeToBlackProgress}) is complete,
     * it changes the game state accordingly and resets relevant fight state properties.
     */
    private void handleTransitioningLogic(){
        // Handle transition to DeathState
        if(this.transitioningToDeath && this.fadeToBlackProgress==1){
            this.transitioningToDeath = false;
            this.fadeToBlackProgress = 0.0;
            this.attackAnimation.setIsFinished(true);
            this.reflink.getHero().resetHealthBarDefaultValues(); // Reset hero health bar for other states.
             this.restoreState();
            this.progressOnSpace = 0;
            this.isTimer3Started = false;
            reflink.getGame().getDeathState().restoreState(); // Prepare DeathState.
            State.setState(reflink.getGame().getDeathState()); // Change to DeathState.
        }

        // Handle transition to PlayState (victory)
        if(this.transitioningToVictory && this.fadeToBlackProgress==1){
            this.transitioningToVictory = false;
            this.fadeToBlackProgress = 0.0;
            this.attackAnimation.setIsFinished(true);
            this.reflink.getHero().setGold(this.reflink.getHero().getGold()+Constants.GOLD_REWARD); // Award gold.
            State.setState(this.fightStrategy.getOwnerState()); // Return to the state where the fight originated.
            this.reflink.getHero().resetHealthBarDefaultValues(); // Reset hero health bar.
            this.restoreState();
        }
    }

    /**
     * @brief Manages the turn-based logic, including player input and enemy actions.
     *
     * If it's the player's turn, it handles mouse input for attack and flee buttons.
     * If it's the enemy's turn, it manages the blocking mini-game (progress bar and spacebar input)
     * and sequences the enemy's attack using timers.
     */
    private void handleTurnLogic(){
        MouseInput mouse = reflink.getMouseInput();
        int mx = mouse.getMouseX();
        int my = mouse.getMouseY();

        if(this.isPlayerTurn){
            // Update button hover states if flee action is not active
            if(!this.isFleeButtonPressed){
                this.fleeButton.updateHover(mx,my);
                this.attackButton.updateHover(mx,my);
            }

            if(mouse.isOneClick()){ // Check for a single mouse click
                // Handle Flee Button Click
                if(this.fleeButton.isClicked(mx,my,true) && !this.isFleeButtonPressed){
                    this.waitForEnemyDeathTimer.start();
                    this.fleeTimer.start(); // Start flee related timer.
                    this.reflink.getHero().setEngageReady(false); // Prevent immediate re-engagement.
                    this.reflink.getHero().setNrOfEscapes(this.reflink.getHero().getNrOfEscapes()-1); // Decrement escapes.
                    this.isFleeButtonPressed = true; // Mark flee as actioned.
                }
                // Handle Attack Button Click
                else if(this.attackButton.isClicked(mx,my,true) && !this.isFleeButtonPressed){
                    this.attackAnimation.triggerOnce(); // Play attack animation.
                    this.fightStrategy.takeDamage((float)this.reflink.getHero().getDamage()); // Enemy takes damage.
                    this.printingDamageDealtPopup = true; // Show damage dealt popup.
                    this.latestDamageDealt = (double)this.fightStrategy.calculateDamage((float)this.reflink.getHero().getDamage());
                    this.attackButton.setIsHovered(false); // Reset button hover.
                    if(this.fightStrategy.getEnemy().getHealth()>0){ // If enemy survives
                        this.isPlayerTurn = false; // Switch to enemy's turn.
                    }
                }
            }
        }
        else{ // Enemy's Turn
            // Handle blocking mechanic input
            if(this.reflink.getKeyManager().isKeyPressed(KeyEvent.VK_SPACE) && this.enemyTurnProgress<=50){
                this.progressOnSpace = this.enemyTurnProgress; // Capture block effectiveness.
                this.enemyTurnProgress = 51; // Stop further progress bar increase for this turn.
            }

            // Update blocking progress bar
            this.blockingBar.updateValue(this.enemyTurnProgress);
            this.enemyTurnProgressTick++;
            if(this.enemyTurnProgressTick>this.enemyTurnProgressCap){
                this.enemyTurnProgress+=6;
                if (this.enemyTurnProgress > 50) this.enemyTurnProgress = 50; // Cap progress at 50.
                this.enemyTurnProgressTick = 0;
            }

            // Sequence enemy attack using timers
            if(!this.isTimerStarted && !isTimerFinished && !didEnemyAttackAlready){
                this.timer.start(); // Start delay before enemy action.
                this.isTimerStarted = true;
            }
            if(this.isTimerFinished && !this.isTimerStarted && !this.didEnemyAttackAlready){
                this.enemy.attack(); // Enemy performs attack animation/logic.
                this.isTimerFinished = false;
                this.isWaitingForPlayerTurn = true; // Start waiting period before player's turn.
                this.didEnemyAttackAlready = true; // Mark enemy attack as done for this turn.
            }
            if(this.isWaitingForPlayerTurn){
                this.timer2.start(); // Start timer to delay player's turn start.
            }
        }
    }

    /**
     * @brief Draws the scrolling background for the fight scene.
     *
     * It uses the background image path defined in the current {@link #fightStrategy}.
     * Two copies of the background are drawn side-by-side to create a seamless scrolling effect.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawBG(Graphics g){
        if(this.fightStrategy.getBackgroundImgPath()!=null){
            BufferedImage backgroundImg = this.reflink.getTileCache().getBackground(this.fightStrategy.getBackgroundImgPath());
            g.drawImage(backgroundImg,scrollX,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
            // Draw a second image to the right of the first for seamless scrolling
            g.drawImage(backgroundImg,scrollX+ Constants.WINDOW_WIDTH,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT,null);
        }
    }

    /**
     * @brief Handles the initial fade-from-black effect when the FightState starts.
     *
     * It gradually reduces the {@link #blackIntensity} to make the screen visible.
     * @param g The {@link Graphics} context to draw on.
     */
    private void handleInitialFadeFromBlack(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Color originalColor = g2d.getColor(); // Save original color

        // Clamp blackIntensity to [0, 1] range
        if(this.blackIntensity>=1){
            this.blackIntensity = 1;
        }
        if(this.blackIntensity<0){
            this.blackIntensity = 0;
        }
        g2d.setColor(new Color(0,0,0,(int)(blackIntensity*255.0) )); // Set color with current alpha
        this.blackIntensity-=this.fadeSpeed; // Decrease intensity for next frame

        g.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT); // Draw overlay
        g2d.setColor(originalColor); // Restore original color
    }

    /**
     * @brief Draws various informational strings on the fight screen.
     *
     * This includes the enemy's name, "Run Away" text with remaining escapes,
     * current turn indication, "Your health" label, and instructions for the blocking mechanic.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawStrings(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial",Font.BOLD,30));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        this.fightStrategy.getEnemy().getEnemyStrategy().drawName(g2d); // Draw enemy name

        // "Run Away" text and escape count
        g2d.setFont(new Font("Arial",Font.BOLD,20));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Run Away",70,500);
        g2d.setFont(new Font("Arial",Font.BOLD,10));
        g2d.drawString("(You can run away " + this.reflink.getHero().getNrOfEscapes() + " more times)",36,530);

        // Current turn indication
        g2d.setFont(new Font("Arial",Font.BOLD,20));
        if(this.isPlayerTurn){
            g2d.drawString("Current Turn: Player",20,30);
        }
        else{
            g2d.setColor(Color.RED);
            String printString = "Current Turn: " + fightStrategy.getEnemy().getName();
            g2d.drawString(printString,20,30);
            g2d.setColor(Color.WHITE);
        }

        // Labels for health and blocking mechanic
        g2d.drawString("Your health",60,640);
        g2d.drawString("Press space",900,687);
        g2d.setFont(new Font("Arial",Font.BOLD,10));
        g2d.drawString("(When the bar is fuller you block more damage)",840,710);
    }

    /**
     * @brief Draws damage popups on the screen.
     *
     * If {@link #printingDamageReceivedPopup} or {@link #printingDamageDealtPopup} is true,
     * this method displays the corresponding damage value ({@link #latestDamageReceived} or
     * {@link #latestDamageDealt}) near the affected character (hero or enemy).
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawPopups(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Color originalColor = g2d.getColor(); // Save original color

        // Draw damage received by player
        if(this.printingDamageReceivedPopup){
            g2d.setFont(new Font("Arial",Font.BOLD,25));
            g2d.setColor(Color.RED);
            String damageToPrint = String.format("-%.2f",this.latestDamageReceived);
            g2d.drawString(damageToPrint,225,677); // Position near player health
            g2d.setColor(originalColor); // Restore original color
        }

        // Draw damage dealt to enemy
        if(this.printingDamageDealtPopup){
            g2d.setFont(new Font("Arial",Font.BOLD,30));
            g2d.setColor(Color.GREEN);
            String damageToPrint = String.format("-%.2f",this.latestDamageDealt);
            g2d.drawString(damageToPrint,320,250); // Position near enemy
            g2d.setColor(originalColor); // Restore original color
        }
    }

    /**
     * @brief Draws the fade-to-black transition effect.
     *
     * If {@link #transitioningToVictory} or {@link #transitioningToDeath} is true,
     * this method gradually increases a black overlay ({@link #fadeToBlackProgress})
     * to cover the screen.
     * @param g The {@link Graphics} context to draw on.
     */
    private void drawTransitionFade(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Color originalColor = g2d.getColor(); // Save original color
        if(transitioningToVictory || transitioningToDeath){
            // originalColor = g2d.getColor(); // Redundant
            this.fadeToBlackProgress +=this.fadeToBlackStep; // Increase fade progress
            if(this.fadeToBlackProgress >=1 ){
                this.fadeToBlackProgress = 1; // Cap progress at 1 (fully opaque)
            }
            int alpha = (int)(this.fadeToBlackProgress*255.0); // Calculate alpha value
            g2d.setColor(new Color(0,0,0,alpha)); // Set color with current alpha
            g.fillRect(0,0,Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT); // Draw overlay
            g2d.setColor(originalColor); // Restore original color
        }
    }

    /**
     * @brief Loads state-specific data.
     *
     * This method is part of the {@link State} contract.
     */
    @Override
    public void loadState(boolean access) {
        // Not implemented for FightState.
    }

    /**
     * @brief Stores state-specific data.
     *
     * This method is part of the {@link State} contract.
     */
    @Override
    public void storeState(boolean access) {
        // Not implemented for FightState.
    }

    /**
     * @brief Sets the enemy for the current fight.
     *
     * This method is called to specify which enemy the player will be fighting.
     * @param enemy The {@link Enemy} entity to engage in combat.
     */
    @Override
    public void setEnemy(Enemy enemy){
        this.enemy = enemy;
    }


    protected String stateName = Constants.FIGHT_STATE; ///< The name identifier for this state.
    /**
     * @brief Gets the name of this state.
     * @return The string identifier for this state, as defined in {@link #stateName}.
     */
    @Override
    public String getStateName() {
        return this.stateName;
    }
}