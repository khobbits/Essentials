package net.ess3.protect;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;


public class EssentialsProtectEntityListener implements Listener
{
	private final transient IProtect prot;

	public EssentialsProtectEntityListener(final IProtect prot)
	{
		super();
		this.prot = prot;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		final Entity target = event.getEntity();

		if (target instanceof Villager && settings.getData().getPrevent().isVillagerDeath())
		{
			event.setCancelled(true);
			return;
		}

		final Player user = target instanceof Player ? (Player)target : null;
		if (target instanceof Player && event instanceof EntityDamageByBlockEvent)
		{
			final DamageCause cause = event.getCause();

			if (cause == DamageCause.CONTACT && (Permissions.PREVENTDAMAGE_CONTACT.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.LAVA && (Permissions.PREVENTDAMAGE_LAVADAMAGE.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.BLOCK_EXPLOSION && (Permissions.PREVENTDAMAGE_TNT.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
					user)))
			{
				event.setCancelled(true);
				return;
			}
		}

		if (target instanceof Player && event instanceof EntityDamageByEntityEvent)
		{
			final EntityDamageByEntityEvent edEvent = (EntityDamageByEntityEvent)event;
			final Entity eAttack = edEvent.getDamager();
			final Player attacker = eAttack instanceof Player ? (Player)eAttack : null;

			// PVP Settings
			if (target instanceof Player && eAttack instanceof Player && (!Permissions.PVP.isAuthorized(user) || !Permissions.PVP.isAuthorized(attacker)))
			{
				event.setCancelled(true);
				return;
			}

			//Creeper explode prevention
			if (eAttack instanceof Creeper && settings.getData().getPrevent().isCreeperExplosion() || (Permissions.PREVENTDAMAGE_CREEPER.isAuthorized(
					user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball) && (Permissions.PREVENTDAMAGE_FIREBALL.isAuthorized(
					user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if ((event.getEntity() instanceof WitherSkull && Permissions.PREVENTDAMAGE_WITHERSKULL.isAuthorized(
					user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (eAttack instanceof TNTPrimed && (Permissions.PREVENTDAMAGE_TNT.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (edEvent.getDamager() instanceof Projectile && ((Permissions.PREVENTDAMAGE_PROJECTILES.isAuthorized(
					user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
					user)) || (((Projectile)edEvent.getDamager()).getShooter() instanceof Player && (!Permissions.PVP.isAuthorized(
					user) || !Permissions.PVP.isAuthorized((Player)((Projectile)edEvent.getDamager()).getShooter())))))
			{
				event.setCancelled(true);
				return;
			}
		}

		final DamageCause cause = event.getCause();
		if (target instanceof Player)
		{
			if (cause == DamageCause.FALL && (Permissions.PREVENTDAMAGE_FALL.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (cause == DamageCause.SUFFOCATION && (Permissions.PREVENTDAMAGE_SUFFOCATION.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
					user)))
			{
				event.setCancelled(true);
				return;
			}
			if ((cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) && (Permissions.PREVENTDAMAGE_FIRE.isAuthorized(
					user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.DROWNING && (Permissions.PREVENTDAMAGE_DROWNING.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.LIGHTNING && (Permissions.PREVENTDAMAGE_LIGHTNING.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
					user)))
			{
				event.setCancelled(true);
			}
			if (cause == DamageCause.WITHER && (Permissions.PREVENTDAMAGE_WITHER.isAuthorized(user)) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		final int maxHeight = settings.getData().getCreeperMaxHeight();

		if (event.getEntity() instanceof EnderDragon && settings.getData().getPrevent().isEnderdragonBlockdamage())
		{
			event.setCancelled(true);
			return;
		}
		else if (event.getEntity() instanceof Wither && settings.getData().getPrevent().isWitherSpawnExplosion())
		{
			event.setCancelled(true);
			return;
		}
		else if (event.getEntity() instanceof Creeper && (settings.getData().getPrevent().isCreeperExplosion() || settings.getData().getPrevent().isCreeperBlockdamage() || (maxHeight >= 0 && event.getLocation().getBlockY() > maxHeight)))
		{
			event.setCancelled(true);
			event.getLocation().getWorld().createExplosion(event.getLocation(), 0F);
			return;
		}
		else if (event.getEntity() instanceof TNTPrimed && settings.getData().getPrevent().isTntExplosion())
		{
			event.setCancelled(true);
			return;
		}
		else if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball) && settings.getData().getPrevent().isFireballExplosion())
		{
			event.setCancelled(true);
			return;
		}
		else if ((event.getEntity() instanceof WitherSkull) && settings.getData().getPrevent().isWitherskullExplosion())
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityTarget(final EntityTargetEvent event)
	{
		final Entity entity = event.getTarget();
		if (entity == null)
		{
			return;
		}

		if (entity.getType() == EntityType.PLAYER)
		{
			final Player user = (Player)event.getTarget();
			if ((event.getReason() == TargetReason.CLOSEST_PLAYER || event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY || event.getReason() == TargetReason.PIG_ZOMBIE_TARGET || event.getReason() == TargetReason.RANDOM_TARGET || event.getReason() == TargetReason.TARGET_ATTACKED_OWNER || event.getReason() == TargetReason.OWNER_ATTACKED_TARGET) && !prot.getSettings().getData().getPrevent().isEntitytarget() && !Permissions.ENTITY_TARGET_BYPASS.isAuthorized(
					user, event.getEntity().getType().getName().toLowerCase()))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(final ExplosionPrimeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball) && settings.getData().getPrevent().isFireballFire())
		{
			event.setFire(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		if (event.getEntityType() == EntityType.ENDERMAN && settings.getData().getPrevent().isEndermanPickup())
		{
			event.setCancelled(true);
		}
		if (event.getEntityType() == EntityType.WITHER && settings.getData().getPrevent().isWitherBlockreplace())
		{
			event.setCancelled(true);
			return;
		}
	}
}
